package e2e;

import io.cucumber.java.BeforeAll;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

public class E2EConfig {

    private static String DOCKER_REGISTRY_URL = Optional.ofNullable(System.getenv("DOCKER_REGISTRY_URL")).orElse("localhost:5000");
    public static String RBP_PROXY_URL = Optional.ofNullable(System.getenv("RBP_PROXY_URL")).orElse("http://localhost:8080");
    public static String DISABLE_TESTCONTAINERS = Optional.ofNullable(System.getenv("DISABLE_TESTCONTAINERS")).orElse("false");

    private static final File DOCKER_COMPOSE_FILE = new File("src/test/resources/docker/docker-compose-test.yaml");

    @BeforeAll
    public static void setupServices() {
        if (Objects.equals(DISABLE_TESTCONTAINERS, "true")) return;

        DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(DOCKER_COMPOSE_FILE)
                .withEnv("DOCKER_REGISTRY_URL", DOCKER_REGISTRY_URL)
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
