package com.automationintesting.model;

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

    public PreparedStatement getPreparedStatement(Connection connection) throws SQLException {
        final String CREATE_TOKEN = "INSERT INTO PUBLIC.TOKENS (token, expiry) VALUES(?, ?);";

        PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TOKEN);
        preparedStatement.setString(1, token);
        preparedStatement.setObject(2, expiry);

        return preparedStatement;
    }

    private LocalDateTime createExpiryTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        return now.plus(tokenDuration);
    }
}
