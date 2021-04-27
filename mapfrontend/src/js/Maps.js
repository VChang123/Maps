import React, {useState} from "react";
import TextBox from "./TextBox";
import { AwesomeButton } from "react-awesome-button";
import "react-awesome-button/dist/styles.css";
import "../css/reacttoggle.css"
import axios from 'axios';
import logo from "../cute_earth.png";
import MapCanvas from "./MapCanvas";
import Toggle from 'react-toggle';
import Feed from "./Feed";



function Maps() {
    //useState Variables that are used throughout the maps class
    const [startLat, setStartLat] = useState(0);
    const [startLon, setStartLon] = useState(0);
    const [endLat, setEndLat] = useState(0);
    const [endLon, setEndLon] = useState(0);
    const [startST, setStartST] = useState("");
    const [startCS, setStartCS] = useState("");
    const [endST, setEndST] = useState("");
    const [endCS, setEndCS] = useState("");
    const [searchbystreet, setsearchbystreet] = useState(false);
    const [route, setRoute] = useState({});

    const [errorMessage, seterrorMessage] = useState("");
    /**
     * async post request to get the intersection between 2 streets
     */
    async function getIntersection(street1, street2){
        let toSend = {
            st1: street1,
            st2: street2
        }
        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
        try {
            const response = await axios.post(
                "http://localhost:4567/intersection",
                toSend,
                config
            );
            const result = await response.data
            if(result["intersection"].length <= 0){
                seterrorMessage("ERROR: No Intersection Found")
            }
            return result["intersection"]

        } catch (error){
            console.log(error)
        }

    }
    /**
     * Makes an post request to get the route based on coordinates
     */
    const requestRoute = (sLat, sLon, eLat, eLon) => {
        //if search by street is false,
        let toSend;
        toSend = {
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
        axios.post(
            "http://localhost:4567/maps",
            toSend,
            config
        )
            .then(response => {
                console.log(response.data);
                if(response.data["maps"].length <= 0) {
                    console.log(response.data["maps"])
                    seterrorMessage("ERROR: No Route Found")
                } else {
                    setRoute(response.data["maps"]);
                    seterrorMessage("");
                }
            })
            .catch(function (error) {
                console.log("error in inputs")
                seterrorMessage("ERROR: Please check your inputs!");
                console.log(error);

            });
    }
    /**
     * Request Route handler specifically for coordinates only
     */
    const requestRouteHandler =()=>{
        requestRoute(startLat, startLon, endLat, endLon)
    }

    /**
     * Async function that checks street names
     * @returns {Promise<void>}
     */
    async function checkStreetInputs() {
        let slat;
        let slon;
        let elat;
        let elon;
        //if the starts street and cross street boxes are not empty and the user puts
        //something in by clicking
        if(startST !== "" && startCS !== "" && endST === "" && endCS === "" ) {
            console.log("start + click")
            let coords = await getIntersection(startST,startCS)
            console.log(coords)
            if(coords.length > 0){
                slat = coords[0]
                slon = coords[1]
                console.log(slat)
                console.log(slon)
                console.log(endLat)
                console.log(endLon)
                requestRoute(slat, slon, endLat, endLon)
            } else {
                seterrorMessage("ERROR: No intersections ")
            }

        } else if (endST !== "" && endCS !== "" && startST === "" && startCS === "") {
            //if the end street and cross street boxes are not empty and the user puts
            //something in by clicking
            console.log("end + click")
            let coords = await getIntersection(endST,endCS)
            console.log(coords)
            if(coords.length > 0) {
                elat = coords[0]
                elon = coords[1]
                console.log(startLat)
                console.log(startLon)
                console.log(elat)
                console.log(elon)
                requestRoute(startLat, startLon, elat, elon)
            } else {
                seterrorMessage("ERROR: No intersections")
            }
        } else if (startST !== "" && startCS !== "" && endST !== "" && endCS !== "" ){
            //if all the street name boxes are filled out
            console.log("start + end")
            let first = await getIntersection(startST,startCS)
            let end = await getIntersection(endST,endCS)
            console.log(first)
            console.log(end)
            if(first.length > 0 && end.length > 0) {
                slat = first[0]
                slon = first[1]
                elat = end[0]
                elon = end[1]
                requestRoute(slat,slon,elat,elon)
            } else {
                seterrorMessage("ERROR: No intersections ")
            }

        } else {
            seterrorMessage("ERROR: Please check your inputs")
        }

    }

    /**
     * handles the toggle button
     * @param e
     */
    const changeMethod = (e) => {
        if (e.target.checked === true) {
            console.log("handling search by street");
            setsearchbystreet(true);

        } else {
            console.log("handling search by latitude/longitude");
            setsearchbystreet(false);
        }
    }
    //displays the error message
    let errordisplay = <div>
        {errorMessage}
    </div>
    //changes what is shown in the website depending on the toggle
    let lats = <div>
        <TextBox label={"Start Latitude"} change={setStartLat} value = {startLat}/>
        <TextBox label={"Start Longitude"} change={setStartLon} value = {startLon}/>
        <TextBox label={"End Latitude"} change={setEndLat} value = {endLat}/>
        <TextBox label={"End Longitude"} change={setEndLon} value = {endLon}/>
        <AwesomeButton label={"Button Handler"} type="secondary" onPress={requestRouteHandler}>
            Submit</AwesomeButton>
    </div>
    if (searchbystreet) {
        lats = <div>
            <TextBox label={"Start Street"} change={setStartST} value = {startST}/>
            <TextBox label={"Start CrossStreet"} change={setStartCS} value = {startCS}/>
            <TextBox label={"End Street"} change={setEndST} value = {endST}/>
            <TextBox label={"End Crosstreet"} change={setEndCS} value = {endCS}/>
            <AwesomeButton label={"Button Handler"} type="secondary" onPress={checkStreetInputs}>
                Submit</AwesomeButton>
        </div>
    }

    return (
        <div className="Maps">
            <div className="side">
                <h1> Maps! </h1>
                <label style={{fontSize: 15, float: "left", maxWidth: 150, lineHeight: 1, paddingLeft: 10}}>search by latitude longitude</label><Toggle
                    id='streetorlat'
                    icons = {false}
                    onChange={changeMethod} />
                <label style={{fontSize: 15, float: "right", maxWidth: 150, lineHeight: 1, paddingRight: 20}}>search by street</label>
                {lats}
                {errordisplay}
                <div>
                    <img src={logo} className="App-logo" alt="logo" />
                </div>
            </div>
            <div className="main">
                <div className="canvasPlacement">
                    <MapCanvas id="canvas" route = {route} slat = {setStartLat} slon = {setStartLon}
                               elat = {setEndLat} elon = {setEndLon} sST = {setStartST} sCS = {setStartCS}
                    eST = {setEndST} eCS = {setEndCS}/>
                    <Feed/>
                </div>

            </div>
        </div>
    );
}
export default Maps;
