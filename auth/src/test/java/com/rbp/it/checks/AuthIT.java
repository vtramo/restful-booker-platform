package com.rbp.it.checks;

import com.rbp.api.AuthApplication;
import com.rbp.config.AppConfig;
import com.rbp.it.payloads.Auth;
import com.rbp.it.payloads.Token;
import com.rbp.it.requests.AuthApi;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Optional;
import java.util.stream.Stream;

import static com.rbp.it.payloads.Token.TOKEN_NAME;
import static io.restassured.parsing.Parser.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    classes = AuthApplication.class
)
@ActiveProfiles("test")
@Testcontainers
@DisplayName("Auth API")
public class AuthIT {

    @Container
    static PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:15.2-alpine")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("init-db.sql")
            .withDatabaseName("test")
            .withExposedPorts(5432);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("auth.db.jdbc-url", postgres::getJdbcUrl);
        registry.add("auth.db.jdbc-username", postgres::getUsername);
        registry.add("auth.db.jdbc-password", postgres::getPassword);
    }

    AuthApi authApi;

    @BeforeEach
    void setupAuthApi() {
        RestAssured.defaultParser = JSON;
        String authUrl = Optional
            .ofNullable(System.getenv(AuthApi.AUTH_SERVICE_URL_ENV_VAR_NAME))
            .orElse("http://localhost:3004");
        authApi = new AuthApi(authUrl);
    }

    @Nested
    @DisplayName("When trying to create a token using incorrect credentials")
    class WhenCreateTokenIncorrectCredentials {

        @ParameterizedTest
        @MethodSource("provideIncorrectCredentials")
        @DisplayName("Should deny the creation of a token to a non-existing user")
        void shouldDenyCreateTokenNonExistingUser(String username, String password) {
            Auth authentication = new Auth(username, password);

            Response response = authApi.createToken(authentication);

            assertThat(response.statusCode(), is(equalTo(HttpStatus.SC_FORBIDDEN)));
        }

        private static Stream<Arguments> provideIncorrectCredentials() {
            return Stream.of(
                Arguments.of(" ", " "),
                Arguments.of(null, null),
                Arguments.of("", ""),
                Arguments.of("\t", "\t"),
                Arguments.of("\n", "\n"),
                Arguments.of("ppipppozzzo354L", "18sujudeDDF1L"),
                Arguments.of("admin", ""),
                Arguments.of("admin", "admin"),
                Arguments.of("root", ""),
                Arguments.of("root", "root")
            );
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("When an account exists")
    class WhenAnAccountExists {

        Connection connection;

        @BeforeAll
        void createConnectionToPostgres() throws SQLException {
            connection = postgres.createConnection("?" + postgres.getJdbcUrl());
        }

        @AfterAll
        void closeConnectionToPostgres() throws SQLException {
            connection.close();
        }

        String username = "pippo11";
        String password = "pppassword";

        @BeforeEach
        void addAccount() throws SQLException {
            var statement = connection.prepareStatement(
                "INSERT INTO ACCOUNTS (username, password) VALUES (?, ?);"
            );
            statement.setString(1, username);
            statement.setString(2, password);
            statement.execute();
        }

        @AfterEach
        void removeAccount() throws SQLException {
            var statement = connection.prepareStatement(
                "DELETE FROM ACCOUNTS WHERE username = ?;"
            );
            statement.setString(1, username);
            statement.execute();
        }

        @Test
        @DisplayName("Should allow him to create a token")
        void shouldAllowHimToCreateAToken() {
            Auth authentication = new Auth(username, password);

            Response response = authApi.createToken(authentication);

            String createdToken = response.getCookie(TOKEN_NAME);
            assertThat(response.statusCode(), is(equalTo(HttpStatus.SC_OK)));
            assertThat(createdToken, not(isEmptyString()));
        }

        @Nested
        @DisplayName("When he/she creates a token")
        class WhenRequestToken {

            Token token;

            @BeforeEach
            void requestToken() {
                Response response = authApi.createToken(new Auth(username, password));
                token = new Token(response.getCookie(TOKEN_NAME));
            }

            @Test
            @DisplayName("When checking it with /auth/validate endpoint, it should be valid")
            void shouldBeValid() {
                Response response = authApi.validateToken(token);

                assertThat(response.statusCode(), is(equalTo(HttpStatus.SC_OK)));
            }
        }

        @Nested
        @DisplayName("When he/she creates a token with a lifespan of 3 seconds")
        class WhenCreatesATokenWithLifespanEqualTo3sec {

            @Autowired
            AppConfig appConfig;
            Token token;

            @BeforeEach
            void requestToken() {
                appConfig.setTokenLifeDuration(Duration.ofSeconds(3));
                Response response = authApi.createToken(new Auth(username, password));
                token = new Token(response.getCookie(TOKEN_NAME));
            }

            @Test
            @DisplayName("Should expire in 3 seconds")
            void shouldExpire() {
                Awaitility.await()
                    .with()
                    .pollDelay(Duration.ofSeconds(3))
                    .atMost(Duration.ofSeconds(4))
                    .untilAsserted(() -> {
                        Response response = authApi.validateToken(token);
                        assertThat(response.statusCode(), is(equalTo(HttpStatus.SC_FORBIDDEN)));
                    });
            }
        }
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("provideNonExistentTokens")
    @DisplayName("When trying to validate a non-existent token with the /auth/validate endpoint, it should not be valid")
    void nonValidToken(String stringToken) {
        Token token = new Token(stringToken);

        Response response = authApi.validateToken(token);

        assertThat(response.statusCode(), is(equalTo(HttpStatus.SC_FORBIDDEN)));
    }

    private static Stream<Arguments> provideNonExistentTokens() {
        return Stream.of(
            Arguments.of("a"),
            Arguments.of("aa"),
            Arguments.of("B"),
            Arguments.of("BC"),
            Arguments.of("ppipppozzzo354L"),
            Arguments.of("fi23r902uig"),
            Arguments.of("admin"),
            Arguments.of("token")
        );
    }
}