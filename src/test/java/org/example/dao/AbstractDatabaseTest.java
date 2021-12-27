package org.example.dao;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

/**
 * Boilerplate code for DB unit test.
 * This test uses Testcontainers for that reason test requires running docker demon.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractDatabaseTest {

    static final String DOCKER_IMAGE = "postgres:14.1";
    static final String DB_NAME = "postgres";
    static final String USER_NAME = "postgres";
    static final String USER_PASSWORD = "postgres";

    protected PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>(DOCKER_IMAGE)
            .withExposedPorts(5432).withDatabaseName(DB_NAME)
            .withUsername(USER_NAME).withPassword(USER_PASSWORD)
            .waitingFor(Wait.defaultWaitStrategy());

    protected String url;

    @BeforeAll
    void runPreTest() {
        dbContainer.start();
        //url example: jdbc:postgresql://localhost:49162/postgres?loggerLevel=OFF
        url = dbContainer.getJdbcUrl();
        System.out.println("Url of db running in docker: "+ url);
        System.out.println("Image name: " + dbContainer.getDockerImageName());
        System.out.println("Docker name: " + dbContainer.getContainerName());
        //init DB schema
        performMigration(url, USER_NAME, USER_PASSWORD);
    }

    @AfterAll
    void runPostTest() {
        dbContainer.stop();
    }

    protected void performMigration(String url, String user, String password) {
        var flyway = Flyway.configure()
                .locations("filesystem:src/main/resources/db/migration")
                .schemas("el_service")
                .dataSource(url, user, password)
                .load();
        flyway.info();
        flyway.migrate();
    }

}
