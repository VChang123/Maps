import React, {useRef, useEffect} from 'react'
import axios from "axios";
import {AwesomeButton} from "react-awesome-button";
import "../css/App.css"

//constants
const HEIGHT = 500;
const WIDTH = 800;
const MAP_RATIO = 0.37;
// (slat - elat) / (slong - elong) , wantn it within += 0.1 of this so ratio not weird
const RATIO_MARGIN = 0.1;
//global reference to canvas & context
let canvas;
let ctx;
let canvasRef;
let oldRouteRef;
//stores latitude & longitude of clicks made on the map
let clickstorage;
//stores locations of drags made on the map
let dragstorage;

let time;
let scrollTime = 300;

let waysCache = new Map();
const TILE_SIZE = 0.01
const reducesize = 0.05;
//once 0.1 lat sized screen, will no longer show nontraverseable ways

function MapCanvas(props) {
    canvasRef = useRef(null);
    oldRouteRef = useRef({})
    //Start and End coordinates useRefs
    const StartLat = useRef(0);
    const StartLon = useRef(0);
    const EndLat = useRef(0);
    const EndLon = useRef(0);

    /**
     * Post request to get the ways in a bounding box
     * @param sLat starting latitude
     * @param sLon starting longitude
     * @param eLat ending latitude
     * @param eLon ending longitude
     */
    async function getWays(sLat, sLon, eLat, eLon) {
        const toSend = {
            srclat: sLat,
            srclong: sLon,
            destlat: eLat,
            destlong: eLon
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
        try {
            const response = await axios.post(
                "http://localhost:4567/ways",
                toSend,
                config
            );
            const results = await response.data
            let roundlat = roundCoordsDownTwo(sLat)
            let roundlon = roundCoordsDownTwo(sLon)
            let stringArray = `${roundlat},${roundlon}`
            waysCache.set(stringArray, results["ways"])
            return results["ways"]

        } catch (error) {
            console.log(error)
        }
    }

    /**
     * Post request to get the nearest node
     * @param lat the latitude
     * @param lon the longitude
     */
    const getNearest = (lat, lon) => {
        const toSend = {
            srclat: lat,
            srclong: lon,

        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
        axios.post(
            "http://localhost:4567/nearest",
            toSend,
            config
        )
            .then(response => {
                // console.log(response.data);
                clickstorage = response.data;
                visualClick(response.data);
            })

            .catch(function (error) {
                console.log(error);

            });

    }

    /**
     * Draws the lines of the Map
     * @param infoList the list of ways to be drawn on the Map
     * @param ILat starting latitude
     * @param ILon starting longitude
     * @param ELat ending latitude
     * @param ELon ending longitude
     */
    const drawLines = (infoList, ILat, ILon, ELat, ELon) => {
        console.log("calling drawLines");
        for (let key in infoList) {
            ctx.strokeStyle = "black";
            ctx.lineWidth = 2;
            ctx.beginPath();
            if (infoList.hasOwnProperty(key)) {
                let sLat = parseFloat(infoList[key][0]);
                let sLon = parseFloat(infoList[key][1]);
                let eLat = parseFloat(infoList[key][2]);
                let eLon = parseFloat(infoList[key][3]);
                let type = infoList[key][4];
                let start = LatLontoCanvasCoords(ILat, ILon, ELat, ELon, sLat, sLon);
                let end = LatLontoCanvasCoords(ILat, ILon, ELat, ELon, eLat, eLon);
                ctx.moveTo(start[0], start[1]);
                ctx.lineTo(end[0], end[1]);
                switch (type) {
                    //footway, primary_link, construction, living_street,road
                    case "footway": //no car streets
                    case "pedestrian":
                    case "steps":
                        if (Math.abs(ILat-ELat)<reducesize) {
                            ctx.strokeStyle = "#bea77a";
                            ctx.lineWidth = 2;
                        }
                        //doesn't show pedestrian things when map too big
                        break;
                    case "service":
                    case "tertiary":
                    case "track":
                    case "road":
                    case "construction":
                    case "primary_link":
                    case "cycleway":
                    case "trunk":
                    case "tertiary_link":
                    case "motorway":
                    case "secondary_link":
                    case "unclassified":
                    case "trunk_link":
                    case "path":
                    case "motorway_link":
                        ctx.strokeStyle = "#070707";
                        ctx.lineWidth = 2;
                        break;
                    case "residential": //car streets
                    case "secondary":
                    case "primary":
                    case "living_street":
                        if (Math.abs(ILat-ELat)<reducesize) {
                            ctx.strokeStyle = "#505050";
                            ctx.lineWidth = 2;
                        } else {
                            ctx.strokeStyle = "#505050";
                            ctx.lineWidth = 1; //thinner if map too big
                        }
                        break;
                    default: //not traversable
                        if (Math.abs(ILat-ELat)<reducesize) {
                            //only draws nontrarvrseable nodes when map is small enough
                            ctx.strokeStyle = "#166e5c";
                            ctx.lineWidth = 1;
                        }
                }
            }

            ctx.stroke();
        }
    }
    /**
     * Converts Latitude and Longitude to canvas coordinates.
     * @param ILat the start bounding latitude
     * @param ILon the start bounding longitude
     * @param ELat the end bounding latitude
     * @param ELon the end bounding longitude
     * @param CLat the current bounding latitude
     * @param CLon the current bounding longitude
     * @returns {[number, number]} returns the x, y coords
     * @constructor takes in the parameters
     */
    const LatLontoCanvasCoords = (ILat, ILon, ELat, ELon, CLat, CLon) => {
        let y = HEIGHT * ((CLat - ILat) / (ELat - ILat))
        let x = WIDTH * ((CLon - ILon) / (ELon - ILon))
        return [x, y]
    }

    /**
     * Draws the route
     * @param route contains the nodes of the route
     * @param reCenter determines if the map needs to recenter to accomodate the road
     */
    const drawRoute = (route, reCenter) => {
        console.log("calling drawRoute");
        if (route.length > 0) {
            let sRLat = parseFloat(route[0][1]);
            let sRLon = parseFloat(route[0][2]);
            let eRLat = parseFloat(route[route.length - 1][3]);
            let eRLon = parseFloat(route[route.length - 1][4]);
            let maxLat = Math.max(sRLat, eRLat);
            let minLat = Math.min(sRLat, eRLat);
            let maxLon = Math.max(sRLon, eRLon)
            let minLon = Math.min(sRLon, eRLon)
            // if the ways are out of bounds of the current canvas, redraw the map
            if (reCenter) {
                if (((maxLat > StartLat.current) || (minLon < StartLon.current)) ||
                    ((minLat < EndLat.current) || (maxLon > EndLon.current))) {
                    //clears the canvas
                    ctx.fillStyle = "white"
                    ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height)
                    let newlatlongratio = (maxLat - minLat) / (maxLon - minLon);
                    console.log("new map ratio is...");
                    console.log(newlatlongratio);
                    let addtolong = 0;
                    let addtolat = 0;
                    // const MAP_RATIO = -0.37; // (slat - elat) / (slong - elong) , want it within += 0.1 of this
                    // const RATIO_MARGIN = 0.1;
                    maxLat = maxLat + 0.0007;
                    minLon = minLon - 0.0007;
                    minLat = minLat - 0.0007;
                    maxLon = maxLon + 0.0007;
                    if (newlatlongratio > MAP_RATIO + RATIO_MARGIN) {
                        //need to increase longitude
                        addtolong += ((maxLat - minLat) / MAP_RATIO - (maxLon - minLon)) * 0.5;
                        console.log("adding this to longitude");
                        console.log(addtolong);
                    }
                    if (newlatlongratio < MAP_RATIO - RATIO_MARGIN) {
                        //need to increase latitude
                        addtolat += (MAP_RATIO * (maxLon - minLon) - (maxLat - minLat)) * 0.5;
                        console.log("adding this to latitude");
                        console.log(addtolat);
                    }
                    getFromCache(maxLat + addtolat, minLon - addtolong,
                        minLat - addtolat, maxLon + addtolong, ctx)
                }
            }
            //drawing route
            console.log("print route")
            console.log(route)
            for (let way in route) {
                let sLat = parseFloat(route[way][1]);
                let sLon = parseFloat(route[way][2]);
                let eLat = parseFloat(route[way][3]);
                let eLon = parseFloat(route[way][4]);
                let start = LatLontoCanvasCoords(StartLat.current, StartLon.current, EndLat.current, EndLon.current, sLat, sLon);
                let end = LatLontoCanvasCoords(StartLat.current, StartLon.current, EndLat.current, EndLon.current, eLat, eLon);
                ctx.strokeStyle = "#ff1f1f";
                ctx.lineWidth = 5
                ctx.beginPath();
                ctx.moveTo(start[0], start[1]);
                ctx.lineTo(end[0], end[1]);
                ctx.stroke();
            }
        }
        console.log("done drawing route")
    }
    /**
     * Rounds the number up.
     * @param num, number to round
     * @returns {number} the rounded number
     */
    const roundUp = (num) => {
        let round = Math.ceil(num * 1000) / 1000;
        return round
    }
    /**
     * Rounds the number down.
     * @param num, number to round
     * @returns {number} the rounded number
     */
    const roundDown = (num) => {
        let round = Math.floor(num * 1000) / 1000;
        return round
    }
    /**
     * rounds number
     * @param num
     * @returns {number}
     */
    const roundCoordsDownTwo = (num) => {
        let round = Math.round(num * 1000) / 1000;
        return round;
    }

    /**
     * The function that handles caching
     * @param slat the start bounding lat
     * @param slon the start bounding lon
     * @param elat the end bounding lat
     * @param elon the end bounding lon
     * @returns {Promise<void>}
     */
    async function getFromCache(slat, slon, elat, elon) {
        console.log("calling getfromCache");
        StartLat.current = slat;
        StartLon.current = slon;
        EndLat.current = elat;
        EndLon.current = elon;

        for (let i = roundUp(slat); i >= roundDown(elat - TILE_SIZE); i = roundCoordsDownTwo(i - TILE_SIZE)) {
            for (let j = roundDown(slon); j <= roundUp(elon + TILE_SIZE); j = roundCoordsDownTwo(j + TILE_SIZE)) {

                console.log("key:")
                console.log(i, j)
                let ways;
                let key = `${i},${j}`
                console.log(waysCache)
                //if the key is in the hashmap
                if (waysCache.has(key)) {
                    console.log("inside cache")
                    //get the ways from the cache
                    ways = waysCache.get(key);
                    console.log("ways inside cache")
                    console.log(ways)
                } else {
                    console.log("making new tiles from getFromCache");
                    //otherwise make a post request
                    ways = await getWays(i, j, i - TILE_SIZE, j + TILE_SIZE);
                }
                //draws the ways returned
                drawLines(ways, StartLat.current,
                    StartLon.current, EndLat.current, EndLon.current)
            }
        }
        console.log("end cache")

    }

    /**
     * handles clicking on canvas, calls getNearest, who calls visual click
     */
    const click = (event) => {
        let difference = [dragstorage[0] - event.offsetX, dragstorage[1] - event.offsetY]
        console.log("clicking");
        console.log(difference);
        if ((Math.abs(difference[0]) + Math.abs(difference[1])) <= 2) {
            const xcoord = event.offsetX; //x and y in terms of the canvas
            const ycoord = event.offsetY;
            let coord = CanvasCoordstoLatLon(xcoord, ycoord, StartLat.current, StartLon.current, EndLat.current, EndLon.current); //switch to actual latlong function when working
            getNearest(coord[0], coord[1]);
            ctx.font = '16px Andale Mono';
            ctx.fillStyle = "#000000";
        }
    }

    /**
     * visually adds a click on the canvas
     * @param latlong contains the coordinates we want to add
     */
    const visualClick = (latlong) => {
        ctx.fillStyle = "#ea4a4a";
        let canvascord = LatLontoCanvasCoords(StartLat.current, StartLon.current, EndLat.current,
            EndLon.current, latlong["nearest"][0], latlong["nearest"][1]);
        ctx.beginPath();
        ctx.fillRect(canvascord[0], canvascord[1], 6, 6);//places a red rectangle/dot
    }
    /**
     * handles adding a click to start coordinates
     */
    const addtoStart = () => {
        console.log(clickstorage);
        props.slat(clickstorage["nearest"][0]);
        props.slon(clickstorage["nearest"][1]);
    }
    /**
     * handles adding a click to end coordinates
     */
    const addtoEnd = () => {
        props.elat(clickstorage["nearest"][0]);
        props.elon(clickstorage["nearest"][1]);
    }
    /**
     * handles clearning the map
     */
    const clearMap = () => {
        props.slat(0);
        props.slon(0);
        props.elat(0);
        props.elon(0);
        clickstorage = {"nearest": [0, 0]}
        ctx.fillStyle = "white"
        ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height)
        getFromCache(StartLat.current, StartLon.current, EndLat.current, EndLon.current)
        oldRouteRef.current = {}

    }
    /**
     * Converts the canvas coordinates to longitude and latitude,
     * @param x canvas x
     * @param y canvas y
     * @param Ilat start lat
     * @param Ilon start lon
     * @param Elat end lat
     * @param Elon end lon
     * @returns {[*, *]} the longitude and latitude
     * @constructor takes in the parameters
     */
    const CanvasCoordstoLatLon = (x, y, Ilat, Ilon, Elat, Elon) => {
        let lat = ((Elat - Ilat) * (y / HEIGHT)) + Ilat;
        let lon = ((Elon - Ilon) * (x / WIDTH)) + Ilon;
        return [lat, lon]
    }

    /**
     * handles scrolling.
     * @param event registers the scroll
     */
    const scroll = (event) => {

        let ychange = event.deltaY; // + means larger, - means smaller
        console.log(event);
        let latchange = 0.00001 * ychange * MAP_RATIO;
        let lonchange = 0.00001 * ychange;



        console.log("getting ways from cache")
        if ((StartLat.current + latchange > EndLat.current - latchange) &&
            (StartLon.current - lonchange < EndLon.current + lonchange)) {
            ctx.fillStyle = "white"
            ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height)
            getFromCache(StartLat.current + latchange, StartLon.current - lonchange,
                EndLat.current - latchange, EndLon.current + lonchange)
            console.log("draw old route")
            drawRoute(oldRouteRef.current, false)
            if (clickstorage !== undefined) {
                visualClick(clickstorage)
            }
        }

    }
    /**
     * Start drag handler.
     * @param event registers mouse down
     */
    const startdrag = (event) => {
        console.log("started drag");
        console.log(event);
        dragstorage = [event.offsetX, event.offsetY];
    }
    /**
     * End drag handler.
     * @param event registers mouse up
     */
    const enddrag = (event) => {
        console.log("ending drag");
        let difference = [dragstorage[0] - event.offsetX, dragstorage[1] - event.offsetY]
        if ((Math.abs(difference[0]) + Math.abs(difference[1])) > 2) { //check so it's not just a click
            let latchange = -0.00001 * difference[1] * MAP_RATIO;
            let lonchange = 0.00001 * difference[0];
            ctx.fillStyle = "white"
            ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height)
            getFromCache(StartLat.current + latchange, StartLon.current + lonchange,
                EndLat.current + latchange, EndLon.current + lonchange)
            console.log("draw old route")
            console.log(oldRouteRef.current)
            drawRoute(oldRouteRef.current, false)
            if (clickstorage !== undefined) {
                visualClick(clickstorage)
            }
        }
    }

    /**
     * Initial use effect that sets up the map.
     */
    useEffect(() => {
        canvas = canvasRef.current
        ctx = canvas.getContext('2d')
        ctx.canvas.width = WIDTH;
        ctx.canvas.height = HEIGHT;
        ctx.fillStyle = "white"
        ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height)
        console.log("initial map")
        //getWays(41.828108, -71.409904, 41.824048, -71.398913)
        getFromCache(41.828108, -71.409904, 41.824048, -71.398913)
        dragstorage = [0, 0];
        canvasRef.current.addEventListener("click", click);
        canvasRef.current.addEventListener("wheel", function (event) {
            event.preventDefault();
            clearTimeout(time)
            time = setTimeout(scroll, scrollTime, event)
        });
        canvasRef.current.addEventListener("mousedown", startdrag);
        canvasRef.current.addEventListener("mouseup", enddrag);
    }, [])
    /**
     * Use effect that re-renders the map when the route changes
     */
    useEffect(() => { //called if deps change
        props.slat(0);
        props.slon(0);
        props.elat(0);
        props.elon(0);
        props.sST("");
        props.sCS("");
        props.eST("");
        props.eCS("");
        oldRouteRef.current = props.route
        drawRoute(props.route, true)
    }, [props.route])

    return (
        <div>
            <div className='instructions'>
                <br></br>
                <b>Hello! Welcome to Vanessa and Xinru's Map!</b> <br></br>
                <text style={{fontSize: 15}}> Search routes using either
                latitude and longitude or by the intersections of streets.
                Use the "Add to Start" and "Add to End" buttons to add the
                latitude & longitude of your most recent click to the latitude & longitude
                search. You can also search using a combination of clicking and streets. The "Submit"
                    button will display the optimal route on the map.
                "Clear Map" will remove markings (clicks & routes) on your current map </text>

            </div>

                <AwesomeButton  label={"Add to Start"} type="primary" onPress={addtoStart}>
                    Add to Start </AwesomeButton>

                <AwesomeButton  label={"Add to End"} type="primary" onPress={addtoEnd}>
                    Add to End </AwesomeButton>

                <AwesomeButton label={"Clear Map"} type="primary" onPress={clearMap}>
                    Clear Map </AwesomeButton>

                <canvas ref={canvasRef} {...props}/>

        </div>
    )
}

export default MapCanvas;