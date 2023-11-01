package com.automationintesting.service;

import com.automationintesting.config.AppConfig;
import com.automationintesting.db.AuthDB;
import com.automationintesting.model.Auth;
import com.automationintesting.model.Decision;
import com.automationintesting.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    private final AuthDB authDB;

    private final AppConfig appConfig;

    @Autowired
    public AuthService(AppConfig appConfig, AuthDB authDB) {
        this.appConfig = appConfig;
        this.authDB = authDB;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void beginDbScheduler() {
        DatabaseScheduler databaseScheduler = new DatabaseScheduler();
        databaseScheduler.startScheduler(authDB, TimeUnit.MINUTES);
    }

    public HttpStatus verify(Token token) throws SQLException {
        Token returnedToken = authDB.queryToken(token);

        if (returnedToken == null || returnedToken.getExpiry().isBefore(LocalDateTime.now())) {
            return HttpStatus.FORBIDDEN;
        }

        return HttpStatus.OK;
    }

    public HttpStatus deleteToken(Token token) throws SQLException {
        Boolean successfulDeletion = authDB.deleteToken(token);

        if (successfulDeletion) {
            return HttpStatus.OK;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }

    public Decision queryCredentials(Auth auth) throws SQLException {
        if (authDB.queryCredentials(auth)) {
            String randomString = new RandomString(16, ThreadLocalRandom.current()).nextString();
            Token token = new Token(randomString, appConfig.getTokenLifeDuration());
            Boolean successfulStorage = authDB.insertToken(token);

            if (successfulStorage) {
                return new Decision(HttpStatus.OK, token);
            } else {
                return new Decision(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new Decision(HttpStatus.FORBIDDEN);
        }
    }
}
