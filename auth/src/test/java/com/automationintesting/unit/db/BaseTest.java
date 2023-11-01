package com.automationintesting.unit.db;

import com.automationintesting.db.AuthDB;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.sql.SQLException;

public class BaseTest {

    protected static AuthDB authDB;

    private static boolean dbOpen;

    @BeforeAll
    public static void createRoomDB() throws SQLException, IOException {

    }

}
