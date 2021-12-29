package org.example.dao;

import org.example.esl.api.EnglishContent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simple validation for db, jooq and dao. If them work together.
 */
//@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnglishContentDaoIntegrationTest extends AbstractDatabaseTest {

    //Class under test
    EnglishContentDao englishContentDao;

    @BeforeAll
    void runPreTest() {
        super.runPreTest();
        englishContentDao = new EnglishContentDaoExample(USER_NAME, USER_PASSWORD, dbContainer.getJdbcUrl());
    }

    @AfterAll
    void runPostTest() {
        dbContainer.stop();
    }

    @Test
    void validateDao() {
        var englishContent = new EnglishContent();
        englishContent.setMetadata(Map.of("key1", "value1"));
        englishContent.setGlossary(Map.of("word1", "description1","word2", "description2"));
        englishContent.setWhatElse(Map.of("term1", "another one explanation"));
        englishContent.setCultureNotes("Some culture statement");
        var id = englishContentDao.create(englishContent);

        var contentFromDB = englishContentDao.getById(id);

        assertThat(contentFromDB).isNotEmpty();
        assertThat(contentFromDB.get().getMetadata()).isNotNull().containsEntry("key1", "value1");
        assertThat(contentFromDB.get().getGlossary()).isNotNull().containsExactlyInAnyOrderEntriesOf(Map.of("word1", "description1","word2", "description2"));
        assertThat(contentFromDB.get().getWhatElse()).isNotNull().containsEntry("term1", "another one explanation");
    }

}
