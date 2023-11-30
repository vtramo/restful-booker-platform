package com.rbp.db;

import com.rbp.config.DatabaseConfig;
import com.rbp.model.db.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class RoomDB {

    private Connection connection;
    private final Logger logger = LoggerFactory.getLogger(RoomDB.class);
    private final String SELECT_ROOMS = "SELECT * FROM ROOMS";
    private final String SELECT_BY_ROOMID = "SELECT * FROM ROOMS WHERE roomid = ?";
    private final String DELETE_BY_ROOMID = "DELETE FROM ROOMS WHERE roomid = ?";
    private final String DELETE_ALL_ROOMS = "DELETE FROM ROOMS";
    private final String RESET_INCREMENT = "ALTER TABLE ROOMS ALTER COLUMN roomid RESTART WITH 1";

    private final DatabaseConfig databaseConfig;

    @Autowired
    public RoomDB(DatabaseConfig databaseConfig) throws SQLException, IOException {
        this.databaseConfig = databaseConfig;
        createJdbcDataSource();
    }

    private void createJdbcDataSource() throws SQLException, IOException {
        connection = DriverManager.getConnection(
            databaseConfig.getJdbcUrl(),
            databaseConfig.getJdbcUsername(),
            databaseConfig.getJdbcPassword()
        );

        executeSqlFile("db.sql");
        executeSqlFile("seed.sql");
    }

    public Room create(Room room) throws SQLException {
        InsertSql insertSql = new InsertSql(connection, room);
        PreparedStatement createPs = insertSql.getPreparedStatement();

        if(createPs.executeUpdate() > 0){
            ResultSet lastInsertId = connection.prepareStatement("SELECT currval(pg_get_serial_sequence('rooms','roomid'))").executeQuery();
            lastInsertId.next();

            PreparedStatement ps = connection.prepareStatement(SELECT_BY_ROOMID);
            ps.setInt(1, lastInsertId.getInt("currval"));

            ResultSet result = ps.executeQuery();
            result.next();

            Room createdRoom = new Room(result);
            createdRoom.setRoomid(result.getInt("roomid"));

            return createdRoom;
        } else {
            return null;
        }
    }

    public Room query(int id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_BY_ROOMID);
        ps.setInt(1, id);

        ResultSet result = ps.executeQuery();
        result.next();

        return new Room(result);
    }

    public Boolean delete(int bookingid) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DELETE_BY_ROOMID);
        ps.setInt(1, bookingid);

        int resultSet = ps.executeUpdate();
        return resultSet == 1;
    }

    public Room update(int id, Room room) throws SQLException {
        UpdateSql updateSql = new UpdateSql(connection, id, room);
        PreparedStatement updatePs = updateSql.getPreparedStatement();

        if(updatePs.executeUpdate() > 0){
            PreparedStatement ps = connection.prepareStatement(SELECT_BY_ROOMID);
            ps.setInt(1, id);

            ResultSet result = ps.executeQuery();
            result.next();

            Room createdRoom = new Room(result);
            createdRoom.setRoomid(result.getInt("roomid"));

            return createdRoom;
        } else {
            return null;
        }
    }

    public List<Room> queryRooms() throws SQLException {
        List<Room> listToReturn = new ArrayList<>();

        PreparedStatement ps = connection.prepareStatement(SELECT_ROOMS);

        ResultSet results = ps.executeQuery();
        while(results.next()){
            listToReturn.add(new Room(results));
        }

        return listToReturn;
    }

    public void seedDB() throws IOException, SQLException {
        PreparedStatement ps = connection.prepareStatement(DELETE_ALL_ROOMS);
        ps.executeUpdate();

        PreparedStatement ps2 = connection.prepareStatement(RESET_INCREMENT);
        ps2.executeUpdate();

        executeSqlFile("seed.sql");
    }

    private void executeSqlFile(String filename) throws IOException, SQLException {
        Reader reader = new InputStreamReader( new ClassPathResource(filename).getInputStream());
        Scanner sc = new Scanner(reader);

        StringBuilder sb = new StringBuilder();
        while(sc.hasNext()){
            sb.append(sc.nextLine());
        }

        connection.prepareStatement(sb.toString()).executeUpdate();
    }
}
