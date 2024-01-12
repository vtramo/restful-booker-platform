package com.rbp.db;

import com.rbp.model.db.Room;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateSql {

    private PreparedStatement preparedStatement;

    UpdateSql(Connection connection, int id, Room room) throws SQLException {
        String UPDATE_ROOM = "UPDATE PUBLIC.ROOMS SET room_name = ?, type = ?, beds = ?, accessible = ?, image = ?, description = ?, features = ?, roomPrice = ? WHERE roomid = ?";

        preparedStatement = connection.prepareStatement(UPDATE_ROOM);
        preparedStatement.setString(1, room.getRoomName());
        preparedStatement.setString(2, room.getType());
        preparedStatement.setInt(3, room.getBeds());
        preparedStatement.setBoolean(4, room.isAccessible());
        preparedStatement.setString(5, room.getImage());
        preparedStatement.setString(6, room.getDescription());

        Array featuresArray = connection.createArrayOf("VARCHAR", room.getFeatures());
        preparedStatement.setArray(7, featuresArray);

        preparedStatement.setInt(8, room.getRoomPrice());
        preparedStatement.setInt(9, id);
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

}
