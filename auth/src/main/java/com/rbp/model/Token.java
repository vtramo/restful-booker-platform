package com.rbp.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Token {

    private String token;
    private LocalDateTime expiry;
    private Duration tokenDuration;

    public Token(String token, Duration tokenDuration) {
        this.token = token;
        this.tokenDuration = tokenDuration;
        expiry = createExpiryTimestamp();
    }

    public Token(String token, LocalDateTime expiry) {
        this.expiry = expiry;
        this.token = token;
    }

    public Token() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    private LocalDateTime createExpiryTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        return now.plus(tokenDuration);
    }
}
