package com.rbp.it.checks;

import com.rbp.it.payloads.Error;
import com.rbp.it.payloads.Room;
import com.rbp.it.payloads.Rooms;
import com.rbp.it.payloads.Token;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RoomCT extends RoomConfigCT {

    static final Token DUMMY_TOKEN = new Token("");

    @ParameterizedTest
    @CsvFileSource(resources = "/correct-room-instances.csv", numLinesToSkip = 1)
    @DisplayName("When creating a valid room, it should return HTTP 201 Created")
    void whenCreatingAValidRoomShouldReturn201(@AggregateWith(RoomAggregator.class) Room room) {
        Response response = roomApi.createRoom(room, DUMMY_TOKEN);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(response.as(Room.class), hasProperty("roomid", is(notNullValue())));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/incorrect-room-instances.csv", numLinesToSkip = 1)
    @DisplayName("When creating an invalid room, it should return HTTP 400 Bad Request")
    void whenCreatingAnInvalidRoomShouldReturn400(@AggregateWith(RoomAggregator.class) Room room) {
        Response response = roomApi.createRoom(room, DUMMY_TOKEN);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_BAD_REQUEST)));
        Error error = response.getBody().as(Error.class);
        assertThat(error.getFieldErrors(),
            either(hasItemInArray("Room name must be set"))
            .or(hasItemInArray("Type must be set"))
            .or(hasItemInArray("must not be null"))
            .or(hasItemInArray("Type can only contain the room options Single, Double, Twin, Family or Suite"))
            .or(hasItemInArray("must be less than or equal to 999"))
        );
    }

    @Nested
    @DisplayName("When creating a room with id 1")
    class WhenCreatingARoom extends RoomConfigCT {

        Connection connection;

        @BeforeAll
        void createConnectionToPostgres() throws SQLException {
            connection = postgres.createConnection("?" + postgres.getJdbcUrl());
        }

        @AfterAll
        void closeConnectionToPostgres() throws SQLException {
            connection.close();
        }

        @Order(1)
        @BeforeEach
        void deleteAllRoomsFromDatabase() throws SQLException {
            var statement = connection.createStatement();
            statement.execute("TRUNCATE ROOMS");
        }

        final int ROOM_ID = 1;
        final String ROOM_NAME = "Business Suite";
        final String ROOM_TYPE = "Suite";
        final int ROOM_PRICE = 320;
        final int ROOM_BEDS = 3;
        final boolean ROOM_ACCESSIBLE = false;
        final String ROOM_URL_IMAGE = "https://t.ly/67890";
        final String ROOM_DESCRIPTION = "For Professional";
        final String[] ROOM_FEATURES = new String[] { "TV", "mini bar", "Wi-Fi" };

        @Order(2)
        @BeforeEach
        void createRoom() throws SQLException {
            var statement = connection.prepareStatement(
                "INSERT INTO ROOMS VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);"
            );

            statement.setInt(1, ROOM_ID);
            statement.setString(2, ROOM_NAME);
            statement.setString(3, ROOM_TYPE);
            statement.setInt(4, ROOM_BEDS);
            statement.setBoolean(5, ROOM_ACCESSIBLE);
            statement.setString(6, ROOM_URL_IMAGE);
            statement.setString(7, ROOM_DESCRIPTION);
            statement.setArray(8, connection.createArrayOf("VARCHAR", ROOM_FEATURES));
            statement.setInt(9, ROOM_PRICE);

            statement.execute();
        }

        @Test
        @DisplayName("When deleting the room with DELETE /room/1, it should return HTTP 202 Accepted")
        void deleteRoom() {
            Response deleteResponse = roomApi.deleteRoom(ROOM_ID, DUMMY_TOKEN);
            Response getResponse = roomApi.getRoom(1, DUMMY_TOKEN);

            assertThat(deleteResponse.getStatusCode(), is(equalTo(HttpStatus.SC_ACCEPTED)));
            assertThat(getResponse.getStatusCode(), is(equalTo(HttpStatus.SC_NOT_FOUND)));
        }

        @Test
        @DisplayName("When deleting the room twice with DELETE /room/1, it should return HTTP 404 Not Found")
        void deleteRoomTwice() {
            Response firstDelete = roomApi.deleteRoom(ROOM_ID, DUMMY_TOKEN);
            Response secondDelete = roomApi.deleteRoom(ROOM_ID, DUMMY_TOKEN);

            assertThat(firstDelete.getStatusCode(), is(equalTo(HttpStatus.SC_ACCEPTED)));
            assertThat(secondDelete.getStatusCode(), is(equalTo(HttpStatus.SC_NOT_FOUND)));
        }

        @Test
        @DisplayName("When getting the room with GET /room/1, it should return HTTP 200 OK")
        void getRoom() {
            Response response = roomApi.getRoom(ROOM_ID, DUMMY_TOKEN);

            assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
            Room room = response.as(Room.class);
            assertThat(room.getRoomName(), is(equalTo(ROOM_NAME)));
            assertThat(room.getRoomPrice(), is(equalTo(ROOM_PRICE)));
            assertThat(room.getDescription(), is(equalTo(ROOM_DESCRIPTION)));
            assertThat(room.getType(), is(equalTo(ROOM_TYPE)));
            assertThat(room.getImage(), is(equalTo(ROOM_URL_IMAGE)));
            assertThat(room.getRoomid(), is(equalTo(ROOM_ID)));
            assertThat(room.getFeatures(), is(equalTo(ROOM_FEATURES)));
            assertThat(room.getBeds(), is(equalTo(ROOM_BEDS)));
        }

        @Test
        @DisplayName("When call GET /room, it should return only one room")
        void getRooms() {
            Response response = roomApi.getRooms(DUMMY_TOKEN);

            assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
            Rooms rooms = response.as(Rooms.class);
            assertThat(rooms.size(), is(equalTo(1)));
            Room firstRoom = rooms.first();
            assertThat(firstRoom.getRoomName(), is(equalTo(ROOM_NAME)));
            assertThat(firstRoom.getRoomPrice(), is(equalTo(ROOM_PRICE)));
            assertThat(firstRoom.getDescription(), is(equalTo(ROOM_DESCRIPTION)));
            assertThat(firstRoom.getType(), is(equalTo(ROOM_TYPE)));
            assertThat(firstRoom.getImage(), is(equalTo(ROOM_URL_IMAGE)));
            assertThat(firstRoom.getRoomid(), is(equalTo(ROOM_ID)));
            assertThat(firstRoom.getFeatures(), is(equalTo(ROOM_FEATURES)));
            assertThat(firstRoom.getBeds(), is(equalTo(ROOM_BEDS)));
        }

        @Nested
        @DisplayName("Given we want to update the room with id 1")
        class WhenUpdateRoom {
            final String NEW_ROOM_NAME = "Economy Twin";
            final int NEW_ROOM_PRICE = 100;
            final String NEW_ROOM_TYPE = "Twin";
            final int NEW_ROOM_BEDS = 2;
            final String NEW_ROOM_DESCRIPTION = "Value Stay";
            final String[] NEW_ROOM_FEATURES = new String[] {"TV", "Wi-Fi"};
            Room newRoom;

            @BeforeEach
            void instantiateNewRoom() {
                newRoom = new Room();
                newRoom.setRoomid(ROOM_ID);
                newRoom.setRoomPrice(NEW_ROOM_PRICE);
                newRoom.setImage(ROOM_URL_IMAGE);
                newRoom.setFeatures(NEW_ROOM_FEATURES);
                newRoom.setType(NEW_ROOM_TYPE);
                newRoom.setDescription(NEW_ROOM_DESCRIPTION);
                newRoom.setAccessible(ROOM_ACCESSIBLE);
                newRoom.setRoomName(NEW_ROOM_NAME);
                newRoom.setBeds(NEW_ROOM_BEDS);
            }

            @Test
            @DisplayName("When updating the room with PUT /room/1, it should return HTTP 202 Accepted")
            void updateRoom() {
                Response response = roomApi.updateRoom(newRoom, ROOM_ID, DUMMY_TOKEN);

                assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_ACCEPTED)));
                Room room = response.as(Room.class);
                assertThat(room.getRoomName(), is(equalTo(NEW_ROOM_NAME)));
                assertThat(room.getRoomPrice(), is(equalTo(NEW_ROOM_PRICE)));
                assertThat(room.getDescription(), is(equalTo(NEW_ROOM_DESCRIPTION)));
                assertThat(room.getType(), is(equalTo(NEW_ROOM_TYPE)));
                assertThat(room.getImage(), is(equalTo(ROOM_URL_IMAGE)));
                assertThat(room.getRoomid(), is(equalTo(ROOM_ID)));
                assertThat(room.getFeatures(), is(equalTo(NEW_ROOM_FEATURES)));
                assertThat(room.getBeds(), is(equalTo(NEW_ROOM_BEDS)));
            }
        }
    }
}