## TODO: Update README with information about your Maps 3 + 4 project! ##


* Which partner’s REPL or Dijkstra's was used 
  - The REPL came from Xinru and the Dijkstra's came from Vanessa
  - The rest of the code came from Vanessa
* Partner division of labor
  - Vanessa: Set up the backend handlers and post requests in the frontend to get the data, backend
    for user checkin feed and user data
  - Xinru: Scrolling, panning, toggles, clicking, and frontend design.
  - Together: Caching, draw map, draw route, also we did a lot of pair programming so 
  both partners were involved in most of the code
* Known bugs
  - scroll only works well on a mouse, doesn't register as much on a mousepad (you can 
    kind of see it but its not as obvious)
  - If you refresh the page, UserCheckin Feed starts from scratch and only displays the 
    users that just had checkins, whereas the Current User Data displays all the 
    checkins that were ever created by that user, including ones before the page refresh
  - You may potentially get a route when it seems that you should not when running "Waterman Street" "Waterman Street"
    "Brown Street" "Waterman Street". This is because when looking at the intersections are on Google Maps
    there is a small street branch around a mall and that could be taken as our intersection because the database is not
    very clear about it. However, when we run "Snake Hole Road" "Black Rock Road" "Lewis Farm Road" "United States of America"
    it properly throws an error so this might be because of a messy database.
* Design details specific to your code
  - Map.js: The Map.js is the main class of the entire app, it calls the MapCanvas.js and Feed.js. In this class,
    the post request for getting the route and intersections were done as well as the logic for the toggle for
    the street names and coordinates. The reason why we made this class call MapCanvas and Feed so we could
    pass in parameters to these classes and use the data to help other functions in the other classes such as
    the route and the values of the textboxes.
  - MapCanvas.js: This class handles the logic for drawing the map, caching, scrolling, panning, and clicking
  - Feed.js: Handles all the logic for updating the live feed on the website. For the caching portion
    of the project we decided to do a tile_size of 0.01 because that proved to be the most optimal
  - Main.java: This class has all the backend handlers to send all the data requested by the frontend to the frontend
  - Detail: We decided to add a command called 'checkin' in order to start the server checkin thread so after
  running the program, the command 'checkin' needs to be called to start the requests to the server.
* Any runtime/space optimizations you made beyond the minimum requirements
  - Other than caching with tiles, no other optimizations were made.
* How to build/run your program with the GUI
  - Please first open three terminals:
  - To start the GUI cd to the mapfrontend folder in one of the folders
    * 'cd Maps2/mapfrontend'
    * Then call 'npm start'
  - To run the Java portion of the code use './run --gui' to start the program
    * Then run the command to load in a database ex. 'map data/maps/maps.sqlite3'
    * After the map has loaded in then start the server in the last terminal using the command
      'python2.7 cs032_maps_location_tracking_py3 <port> <number of users> [-s]' for Windows or
      if you are on Mac use 'cs032_maps_location_tracking <port> <number of users> [-s]' or 'cs032_maps_location_tracking_py3 <port> <number of users> [-s]'
      * -S is for the large map and -s is for the small map
    * Then after the server is started, then in the terminal where we run './run --gui' and loaded in our
      maps, call the command 'checkin' in order to start the checkin server thread.
      
*To use the website:
  - Please use the set start and set end buttons to enter the coordinates that you click.
  - Use the submit button to set the coordinates or the streetnames
  - Use the clear button to clear the map of the route
  - The user checkin feed should slowly start when the server is started
  - Click on a user feed to get all the data about that user.
* What browser you used to test your GUI
  - Please use Chrome to test our GUI
