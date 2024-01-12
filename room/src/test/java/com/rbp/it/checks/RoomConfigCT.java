package com.rbp.it.checks;

import com.rbp.api.RoomApplication;
import com.rbp.it.requests.RoomApi;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static io.restassured.parsing.Parser.JSON;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    classes = RoomApplication.class
)
@EnableWireMock({@ConfigureWireMock(
    name = RoomConfigCT.AUTH_SERVICE_WIREMOCK_NAME,
    property = RoomConfigCT.AUTH_SERVICE_WIREMOCK_PROPERTY_URL
)})
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
@ActiveProfiles("test")
@DisplayName("Room API")
public class RoomConfigCT {
    static final String JENKINS_NETWORK_ID = "jenkins";
    static final boolean JENKINS_CI_ENVIRONMENT = Optional.ofNullable(System.getenv("CI"))
        .map(ci -> ci.equals("true"))
        .orElse(false);
    static final String AUTH_SERVICE_WIREMOCK_NAME = "auth-service";
    static final String AUTH_SERVICE_WIREMOCK_PROPERTY_URL = "app.auth-service-client-url";
    static final String PG_IS_READY_COMMAND = "pg_isready -U postgres";

    @Container
    static PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:15.2-alpine")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("init-db.sql")
            .withDatabaseName("test")
            .waitingFor(Wait.forSuccessfulCommand(PG_IS_READY_COMMAND))
            .withExposedPorts(5432);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        if (JENKINS_CI_ENVIRONMENT) {
            postgres.withNetwork(new Network() {
                @Override
                public String getId() { return JENKINS_NETWORK_ID; }
                @Override
                public void close() {}

                @Override
                public Statement apply(Statement statement, Description description) {
                    return null;
                }
            });
        }
        postgres.start();
        registry.add("room.db.jdbc-url", postgres::getJdbcUrl);
        registry.add("room.db.jdbc-username", postgres::getUsername);
        registry.add("room.db.jdbc-password", postgres::getPassword);
    }

    @InjectWireMock(AUTH_SERVICE_WIREMOCK_NAME)
    WireMockServer wiremock;

    @BeforeEach
    void configureWireMockAuthService() {
        wiremock.stubFor(post("/auth/validate")
            .willReturn(aResponse()
                .withStatus(200)));
    }

    RoomApi roomApi;

    @BeforeAll
    void setupRoomApi() {
        RestAssured.defaultParser = JSON;
        String roomUrl = Optional
            .ofNullable(System.getenv(RoomApi.ROOM_SERVICE_URL_ENV_VAR_NAME))
            .orElse("http://localhost:3001");
        roomApi = new RoomApi(roomUrl);
    }
}
