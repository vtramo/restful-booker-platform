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
public class AuthDB {

    private final String DELETE_ALL_TOKENS = "DELETE FROM TOKENS";
    private final String SELECT_BY_TOKEN = "SELECT * FROM TOKENS WHERE token = ?";
    private final String DELETE_BY_TOKEN = "DELETE FROM TOKENS WHERE token = ?";
    private final String SELECT_BY_CREDENTIALS = "SELECT * FROM ACCOUNTS WHERE username = ? AND password = ?";

    private Connection connection;
    private Logger logger = LoggerFactory.getLogger(AuthDB.class);

    private final DatabaseConfig databaseConfig;

    public AuthDB(DatabaseConfig databaseConfig) throws SQLException, IOException {
        this.databaseConfig = databaseConfig;
        createJdbcDataSource();
    }

    private void createJdbcDataSource() throws SQLException, IOException {
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
        PreparedStatement createPs = token.getPreparedStatement(connection);

        return createPs.executeUpdate() > 0;
    }

    public Token queryToken(Token token) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_BY_TOKEN);
        ps.setString(1, token.getToken());

        ResultSet result = ps.executeQuery();

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
        PreparedStatement ps = connection.prepareStatement(DELETE_BY_TOKEN);
        ps.setString(1, token.getToken());

        int resultSet = ps.executeUpdate();
        return resultSet == 1;
    }

    public Boolean queryCredentials(Auth auth) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_BY_CREDENTIALS);
        ps.setString(1, auth.getUsername());
        ps.setString(2, auth.getPassword());

        ResultSet result = ps.executeQuery();
        result.next();

        return result.getRow() > 0;
    }

    private void executeSqlFile(String filename) throws IOException, SQLException {
        Reader reader = new InputStreamReader( new ClassPathResource(filename).getInputStream());
        Scanner sc = new Scanner(reader);

        StringBuffer sb = new StringBuffer();
        while(sc.hasNext()) {
            sb.append(sc.nextLine());
        }

        connection.prepareStatement(sb.toString()).executeUpdate();
    }

    public void resetDB() throws SQLException, IOException {
        PreparedStatement ps = connection.prepareStatement(DELETE_ALL_TOKENS);
        ps.executeUpdate();

        executeSqlFile("seed.sql");
    }
}
