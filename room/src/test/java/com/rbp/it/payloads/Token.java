package com.rbp.it.payloads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public class Token {
    public static final String TOKEN_NAME = "token";

    @JsonProperty
    private String token;

    @JsonProperty
    @JsonIgnore
    private ZonedDateTime expiry;


    public Token(@JsonProperty("token") String token) {
        this.token = token;
    }

    public Token() {}


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ZonedDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(ZonedDateTime expiry) {
        this.expiry = expiry;
    }
}
