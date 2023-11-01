package com.automationintesting.unit.db;

import com.automationintesting.model.Auth;
import com.automationintesting.model.Token;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthDBTest extends BaseTest {

    @Test
    public void testQueryCredentials() throws SQLException {
        Auth auth = new Auth("admin", "password");

        Boolean credentialQuary = authDB.queryCredentials(auth);

        assertEquals( true, credentialQuary);
    }

    @Test
    public void testQueryMissingCredentials() throws SQLException {
        Auth auth = new Auth("password", "admin");

        Boolean credentialQuary = authDB.queryCredentials(auth);

        assertEquals( false, credentialQuary);
    }

}
