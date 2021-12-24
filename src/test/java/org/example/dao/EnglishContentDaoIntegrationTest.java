package org.example.dao;

import org.example.esl.EnglishContent;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simple validation for db, jooq and dao. If them work together.
 * This test uses Testcontainers for that reason test requires running docker demon.
 */
//@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnglishContentDaoIntegrationTest {

    static final String DOCKER_IMAGE = "postgres:14.1";
    static final String DB_NAME = "postgres";
    static final String USER_NAME = "postgres";
    static final String USER_PASSWORD = "postgres";

    //@Container
    private PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>(DOCKER_IMAGE)
            .withExposedPorts(5432).withDatabaseName(DB_NAME)
            .withUsername(USER_NAME).withPassword(USER_PASSWORD)
            .withReuse(true)
            .waitingFor(Wait.defaultWaitStrategy());

    //Class under test
    EnglishContentDao englishContentDao;

    @BeforeAll
    void runPreTest() {
        dbContainer.start();
        //url example: jdbc:postgresql://localhost:49162/postgres?loggerLevel=OFF
        var url = dbContainer.getJdbcUrl();
        System.out.println("Url of db running in docker: "+ url);
        System.out.println("Image name: " + dbContainer.getDockerImageName());
        System.out.println("Docker name: " + dbContainer.getContainerName());
        //init DB schema
        performMigration(url, USER_NAME, USER_PASSWORD);
        englishContentDao = new EnglishContentDaoExample(USER_NAME, USER_PASSWORD, url);
    }

    @AfterAll
    void runPostTest() {
        dbContainer.stop();
    }

    private void performMigration(String url, String user, String password) {
        var flyway = Flyway.configure()
                .locations("filesystem:src/main/resources/db/migration")
                .schemas("el_service")
                .dataSource(url, user, password)
                .load();
        flyway.info();
        flyway.migrate();
    }

    @Test
    void validateDao() {
        var englishContent = new EnglishContent();
        englishContent.setMetadata(Map.of("key1", "value1"));
        englishContent.setGlossary(Map.of("word1", "description1"));
        var id = englishContentDao.create(englishContent);

        var contentFromDB = englishContentDao.getById(id);

        assertThat(contentFromDB).isNotEmpty();
        assertThat(contentFromDB.get().getMetadata()).isNotNull().containsEntry("key1", "value1");
        assertThat(contentFromDB.get().getGlossary()).isNotNull().containsEntry("word1", "description1");
    }

}
