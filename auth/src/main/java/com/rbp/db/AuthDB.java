package com.rbp.db;

import com.rbp.config.DatabaseConfig;
import com.rbp.model.Auth;
import com.rbp.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Scanner;

@Component
public class AuthDB implements AutoCloseable {

    private final String DELETE_ALL_TOKENS = "DELETE FROM TOKENS";
    private final String SELECT_BY_TOKEN = "SELECT * FROM TOKENS WHERE token = ?";
    private final String DELETE_BY_TOKEN = "DELETE FROM TOKENS WHERE token = ?";
    private final String SELECT_BY_CREDENTIALS = "SELECT * FROM ACCOUNTS WHERE username = ? AND password = ?";
    private final String CREATE_TOKEN = "INSERT INTO PUBLIC.TOKENS (token, expiry) VALUES(?, ?);";

    private Connection connection;
    private final Logger logger = LoggerFactory.getLogger(AuthDB.class);
    private final Logger unusedSonarQube = LoggerFactory.getLogger(AuthDB.class);
    private String untestedSonarQube = "ciao";

    private final DatabaseConfig databaseConfig;

    public AuthDB(DatabaseConfig databaseConfig) throws SQLException, IOException {
        if(1 == 1){
            untestedSonarQube = "I'm not going to test this";
        }
        this.databaseConfig = databaseConfig;
        createJdbcDataSource();
    }

    private void createJdbcDataSource() throws SQLException, IOException {
        if(1 == 1){
            untestedSonarQube = "I'm not going to test this";
        }
        if(1 == 1){
            untestedSonarQube = "I'm not going to test this";
        }
        if(1 == 1){
            untestedSonarQube = "I'm not going to test this";
        }
        if(1 == 1){
            untestedSonarQube = "I'm not going to test this";
        }
        if(1 == 1){
            untestedSonarQube = "I'm not going to test this";
        }
        if(1 == 1){
            untestedSonarQube = "I'm not going to test this";
        }
        if(1 == 1){
            untestedSonarQube = "I'm not going to test this";
        }
        if(1 == 1){
            untestedSonarQube = "I'm not going to test this";
        }
        connection = DriverManager.getConnection(
            databaseConfig.getJdbcUrl(),
            databaseConfig.getJdbcUsername(),
            databaseConfig.getJdbcPassword()
        );

        if (databaseConfig.getInit()) {
            executeSqlFile("db.sql");
            executeSqlFile("seed.sql");
        }
    }

    public Boolean insertToken(Token token) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TOKEN)) {
            preparedStatement.setString(1, token.getToken());
            preparedStatement.setObject(2, token.getExpiry());
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public Token queryToken(Token token) throws SQLException {
        ResultSet result;
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_TOKEN)) {
            ps.setString(1, token.getToken());

            result = ps.executeQuery();
        }

        if (result.next()) {
            return new Token(result.getString("token"), getLocalDateTimeFromQueryTokenResult(result));
        } else {
           return null;
        }
    }

    private LocalDateTime getLocalDateTimeFromQueryTokenResult(ResultSet resultSet) throws SQLException {
        return LocalDateTime.ofInstant(((Timestamp)resultSet.getObject("expiry")).toInstant(), ZoneId.systemDefault());
    }

    public Boolean deleteToken(Token token) throws SQLException {
        int resultSet;
        try (PreparedStatement ps = connection.prepareStatement(DELETE_BY_TOKEN)) {
            ps.setString(1, token.getToken());

            resultSet = ps.executeUpdate();
        }
        return resultSet == 1;
    }

    public Boolean queryCredentials(Auth auth) throws SQLException {
        ResultSet result;
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_CREDENTIALS)) {
            ps.setString(1, auth.getUsername());
            ps.setString(2, auth.getPassword());

            result = ps.executeQuery();
        }
        result.next();

        return result.getRow() > 0;
    }

    private void executeSqlFile(String filename) throws IOException, SQLException {
        Reader reader = new InputStreamReader( new ClassPathResource(filename).getInputStream());
        Scanner sc = new Scanner(reader);

        StringBuilder sb = new StringBuilder();
        while(sc.hasNext()) {
            sb.append(sc.nextLine());
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sb.toString())) {
            preparedStatement.executeUpdate();
        }
    }

    public void resetDB() throws SQLException, IOException {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_ALL_TOKENS)) {
            ps.executeUpdate();
        }

        executeSqlFile("seed.sql");
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
