import React, {useRef, useEffect, useState} from 'react'
import axios from "axios";
import "../css/App.css"

let alluserdata = []

function Feed(props) {
    const [checkinMessage, setcheckinMessage] = useState("");
    const [feedstate, setfeedstate] = useState([]);
    const[userdata, setUserData] = useState([]);

    /**
     * Async function to get the user data from the server
     * @returns {Promise<*>} returns data from the server
     */
    function getUserData() {
        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
        axios.post(
            "http://localhost:4567/userdata",
            config
        )
            .then(response => {
                let result = response.data["userdata"];
                console.log(result);
                alluserdata = alluserdata.concat(result)
                setfeedstate(alluserdata)

            })

            .catch(function (error) {
                console.log(error);

            });


    }

    function getSingleUserData(userid) {
        const toSend = {
            id: userid
        };
        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/singleuser",
            toSend,
            config
        )
        .then(response => {
            let result = response.data["singleuser"];
            console.log(result);
            setUserData(result);

        })

            .catch(function (error) {
                console.log(error);

            });


    }

    /**
     * Displays the time in a more visually appealing way
     * @param e
     */
    const displayTime = (e) => {
        const a = new Date(e * 1000);
        const months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
        const year = a.getFullYear();
        const month = months[a.getMonth()];
        const date = a.getDate();
        const hour = a.getHours();
        const min = a.getMinutes();
        const sec = a.getSeconds();
        const time = month + ' '+ date + ', ' +  + year + ' at ' + hour + ':' + min + ':' + sec ;
        return time;
    }
    useEffect(() => { //called if deps change
        setInterval(getUserData, 5000);

    }, [])


    return (
        <div>
            <br></br>
            <br></br>
            <br></br>

            <div className='instructions'>
                <br></br>
                <b>Real Time User Checkins!</b> <br></br>
                <br></br>
                <br></br>
                <text style={{fontSize: 15}}> Below you can find updates on real users
                    based on their checkins! Click on any user update in the left box to see
                    the checkins of one specific user on the right box.
                </text>

            </div>
            <div>
                <div className="outercheckin">
                    UserCheckin Feed:<br></br>
                    <br></br>
                    <div className="checkin">
                        {feedstate.map((x) => <li key={x["id"] + x["timestamp"]} className={"feed"}
                                                  onClick={() => getSingleUserData(x["id"])} >
                            ID:{x["id"]}, Name:{x["name"]}<br></br> Timestamp:{displayTime(x["timestamp"])},
                            Latitude:{x["latitude"]}, Longitude:{x["longitude"]} </li>)}

                    </div>
                </div>
                <div className="outeruser">
                    Current User Data:<br></br>
                    <br></br>
                    <div className="userdata">
                        {userdata.map((x, index) => <li key = {JSON.stringify(x) + JSON.stringify(index)} className={"user"}>
                            ID:{x[0]}, Name:{x[1]}, <br></br> Timestamp:{displayTime(x[2])},
                            Latitude:{x[3]}, Longitude:{x[4]} </li> )}
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Feed;