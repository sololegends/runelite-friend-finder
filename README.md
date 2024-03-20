# Runelite Friend Finder
A simple plugin for showing your friends on your world map and visa versa

## Features 
- Send your locations to those on your friends list and receive their location back 
  - NOTE: Both you AND your friend must have the plugin for this to work.
- (Configurable) Double click your friend's point on the map to hop to their world
- Configurable report interval. Have low bandwidth? Set the plugin to only report and update every 30 seconds!
- Configurable point colors and sizes
- See your friends health and prayer statuses in a handy sidebar

![image](docs/friend-on-map-example.png)

Your in-game location will be sent to an external server where it will be store as a single data point for all your friends to pull down and see. Your location is only visible to people on your RuneScape friends list, unless you have a person set as your friend they *cannot* see your location.  

The way the location retrieval of your friends works is they would have to have the this plugin and be also sending their their location to the server for your client to retrieve and then display. Both people must have the plugin for this to work.

## API Server 
The current API server used by default is one developed and run by me. However, If you so desire you can setup a private API for this plugin fairly easily. Here is how. 

1. Setup a server with a single REST API
2. Ensure that API server is accessible from wherever you wish to play osrs with the this plugin
3. Share and set the proper API Key in the plugin configuration for your API 
   - Key not used for public API server, this is built in just for those who want to roll their own server
   - this can be left blank to not send an auth header
4. Change the API Server configuration value in your plugin, and have anyone who wishes to join you on that private server also change that configuration option. 
  
### API Server Requirement
- **Player location data REQUEST payload:**
  - API Key sent as Authorization header `Authorization: Bearer $API_KEY`
  ```json 
  {
    // Player's Username
    "name": string,
    // Player's ID
    "id": int32,
    // X-Axis position
    "x": int32,
    // Y-Axis position
    "y": int32,
    // Z-Axis position
    "z": int32,
    // World number
    "w": int32,
    // Region ID adjusted for local instanced regions
    "r": int32,
    // Following not necessary, unless for side-bar functionality
    // Health: current
    "hm": int32,
    // Health: Maximum
    "hM": int32,
    // Prayer: current
    "pm": int32,
    // prayer: maximum
    "pM": int32,
    // Player friends
    "friends": [
      string...
    ]
  }
  ```   
- **Player location data RESPONSE payload:**
  ```json 
  [
    {
      // Friend's Username
      "name": string,
      // Friend's ID
      "id": int32,
      // X-Axis position
      "x": int32,
      // Y-Axis position
      "y": int32,
      // Z-Axis position
      "z": int32,
      // World number
      "w": int32,
      // Region ID adjusted for local instanced regions
      "r": int32,
      // Human readable location, will take priority over client resolved. However not needed
      "l": string,
      // Following not necessary, unless for side-bar functionality
      // Health: current
      "hm": int32,
      // Health: Maximum
      "hM": int32,
      // Prayer: current
      "pm": int32,
      // prayer: maximum
      "pM": int32
    }...
  ]
  ```   

- **Location Report REQUEST payload:**
  - API Key sent as Authorization header `Authorization: Bearer $API_KEY`
  ```json 
  {
    // X-Axis position
    "x": int32,
    // Y-Axis position
    "y": int32,
    // Z-Axis position
    "z": int32,
    // Non-instanced Region ID
    "r": int32,
    // Local instanced region ID or '-1' if not in instance
    "i": int32,
    // Suggested name for the region
    "l": string
  }
  ```   

## Privacy Information
- Yes, this plugin reaches out to a server I setup to store and deliver your runescape location to and from your friends
- No location history data is stored
- Absolutely _NO_ personal information is sent to the server whatsoever. 
- Your public IP address may briefly be stored in memory for use in DoS protection and general server security.  
- All location data is removed after 90 seconds from last report.