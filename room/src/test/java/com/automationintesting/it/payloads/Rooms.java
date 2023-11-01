package com.automationintesting.it.payloads;

import com.automationintesting.it.payloads.Room;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Rooms {
    @JsonProperty private List<Room> rooms;
    
    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public int size() {
        return rooms.size();
    }

    public Room first() {
        return rooms.get(0);
    }
}
