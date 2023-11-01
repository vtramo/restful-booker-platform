package com.automationintesting.it.requests;

import com.automationintesting.it.payloads.Room;
import com.automationintesting.it.payloads.Token;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class RoomApi extends BaseApi {
    public static final String ROOM_SERVICE_URL_ENV_VAR_NAME = "ROOM_SERVICE_URL";

    public RoomApi(String url) {
        super(url);
    }

    public Response getRooms() {
        return given()
            .get(url() + "room/");
    }

    public Response createRoom(Room room, Token token) {
        Cookie tokenCookie = new Cookie.Builder(Token.TOKEN_NAME, token.getToken()).build();
        return given()
            .body(room)
            .contentType(ContentType.JSON)
            .cookie(tokenCookie)
            .post(url() + "room/");
    }

    public Response deleteRoom(int id, Token token) {
        Cookie tokenCookie = new Cookie.Builder(Token.TOKEN_NAME, token.getToken()).build();
        return given()
            .cookie(tokenCookie)
            .delete(url() + "room/" + id);
    }

    public Response getRoom(int id, Token token) {
        Cookie tokenCookie = new Cookie.Builder(Token.TOKEN_NAME, token.getToken()).build();
        return given()
            .cookie(tokenCookie)
            .get(url() + "room/" + id);
    }

    public Response getRooms(Token token) {
        Cookie tokenCookie = new Cookie.Builder(Token.TOKEN_NAME, token.getToken()).build();
        return given()
            .cookie(tokenCookie)
            .get(url() + "room/");
    }

    public Response updateRoom(Room room, int id, Token token) {
        Cookie tokenCookie = new Cookie.Builder(Token.TOKEN_NAME, token.getToken()).build();
        return given()
            .cookie(tokenCookie)
            .contentType(ContentType.JSON)
            .body(room)
            .put(url() + "room/" + id);
    }
}
