package com.rbp.db;

import com.rbp.model.db.Room;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertSql {

    private PreparedStatement preparedStatement;

    private String roomName;
    private String type;
    private int beds;
    private boolean accessible;
    private String image;
    private String description;
    private String[] features;

    InsertSql(Connection connection, Room room) throws SQLException {
        final String CREATE_ROOM = "INSERT INTO PUBLIC.ROOMS (room_name, type, beds, accessible, image, description, features, roomPrice) VALUES(?, ?, ?, ?, ?, ?, ?, ?);";

        preparedStatement = connection.prepareStatement(CREATE_ROOM);
        preparedStatement.setString(1, room.getRoomName());
        preparedStatement.setString(2, room.getType());
        preparedStatement.setInt(3, room.getBeds());
        preparedStatement.setBoolean(4, room.isAccessible());
        preparedStatement.setString(5, room.getImage());
        preparedStatement.setString(6, room.getDescription());

        Array featuresArray = connection.createArrayOf("VARCHAR", room.getFeatures());
        preparedStatement.setArray(7, featuresArray);

        preparedStatement.setInt(8, room.getRoomPrice());
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }
}
