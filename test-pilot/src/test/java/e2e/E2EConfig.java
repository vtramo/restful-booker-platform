package e2e;

import io.cucumber.java.BeforeAll;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.util.Optional;

public class E2EConfig {

    public static String RBP_PROXY_URL = Optional.ofNullable(System.getenv("RBP_PROXY_URL")).orElse("http://localhost:8080");

    private static final File DOCKER_COMPOSE_FILE = new File("src/test/resources/docker/docker-compose-test.yaml");

    @BeforeAll
    public static void setupServices() {
        DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(DOCKER_COMPOSE_FILE)
                .waitingFor("rbp-proxy", Wait.defaultWaitStrategy())
                .withBuild(true)
		        .withExposedService("rbp-proxy", 8080)
                .withExposedService("rbp-booking", 3000)
                .withExposedService("rbp-room", 3001)
                .withExposedService("rbp-assets", 3003)
                .withExposedService("rbp-auth", 3004)
                .withExposedService("rbp-report", 3005)
                .withExposedService("rbp-message", 3006)
                .withExposedService("postgres", 5432);
        compose.start();
    }
}
