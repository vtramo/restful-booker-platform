package com.rbp.requests;

import com.rbp.model.room.Room;
import com.rbp.model.room.Rooms;
import org.springframework.web.client.RestTemplate;

public class RoomRequests {

    private String host;

    public RoomRequests() {
        if(System.getenv("ROOM_SERVICE_URL") == null){
            host = "http://localhost:3001/room";
        } else {
            host = "http://" + System.getenv("ROOM_SERVICE_URL") + ":3001/room";
        }
    }

    public Rooms searchForRooms(){
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForEntity(host, Rooms.class).getBody();
    }

    public Room searchForSpecificRoom(String id){
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForEntity(host + "/" + id, Room.class).getBody();
    }

}
