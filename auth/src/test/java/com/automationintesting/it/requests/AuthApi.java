package com.automationintesting.it.requests;

import com.automationintesting.it.payloads.Auth;
import com.automationintesting.it.payloads.Token;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthApi extends BaseApi {
    public static final String AUTH_SERVICE_URL_ENV_VAR_NAME = "AUTH_SERVICE_URL";

    public AuthApi(String url) {
        super(url);
    }

    public Response createToken(Auth authentication) {
        return given()
            .contentType(ContentType.JSON)
            .body(authentication)
            .post(url() + "auth/login");
    }

    public Response validateToken(Token token) {
        return given()
            .contentType(ContentType.JSON)
            .body(token)
            .post(url() + "auth/validate");
    }

    public Response clearToken(Token token) {
        return given()
            .contentType(ContentType.JSON)
            .body(token)
            .post(url() + "auth/logout");
    }
}
