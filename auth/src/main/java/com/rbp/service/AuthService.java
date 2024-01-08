package com.rbp.service;

import com.rbp.config.AppConfig;
import com.rbp.db.AuthDB;
import com.rbp.model.Auth;
import com.rbp.model.Decision;
import com.rbp.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
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
            SecureRandom secureRandom = new SecureRandom();
            String randomString = new RandomString(16, secureRandom).nextString();
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
