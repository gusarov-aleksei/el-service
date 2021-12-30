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
        englishContentDao = new EnglishContentDaoImpl(USER_NAME, USER_PASSWORD, url);
    }

    @AfterAll
    void runPostTest() {
        super.runPostTest();
    }

    @Test
    void testCreate_shouldCreateNewRecordInDatabase_whenMethodCreateIsCalled() {
        // this test validates create and getById operations
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

    @Test
    void testGetById_shouldRetrieveNothing_whenRecordWithIdDoesNotExistInDatabase() {
        assertThat(englishContentDao.getById(100500)).isEmpty();
        // create any record
        englishContentDao.create(new EnglishContent());
        // and try to get not existing again
        assertThat(englishContentDao.getById(100500)).isEmpty();
    }

    @Test
    void testFetchByFileName_shouldRetrieveRecord_whenRecordExistsInDatabase() {
        var englishContent = new EnglishContent();
        englishContent.setMetadata(Map.of("key1", "value1", "filename", "test1.pdf"));
        englishContent.setGlossary(Map.of("word1", "description1"));

        englishContentDao.create(englishContent);

        var englishContent2 = new EnglishContent();
        englishContent2.setMetadata(Map.of("key1", "value2", "filename", "test2.pdf"));
        englishContent2.setGlossary(Map.of("word2", "description2"));

        englishContentDao.create(englishContent2);

        var contentFromDB = englishContentDao.fetchByFileName("test1.pdf");

        assertThat(contentFromDB).isPresent();
        assertThat(contentFromDB.get().getGlossary()).isNotNull()
                .containsEntry("word1", "description1");

        var contentFromDB2 = englishContentDao.fetchByFileName("test2.pdf");

        assertThat(contentFromDB2).isPresent();
        assertThat(contentFromDB2.get().getGlossary()).isNotNull()
                .containsEntry("word2", "description2");
    }

    @Test
    void testFetchByFileName_shouldRetrieveNothing_whenRecordDoesNotExistInDatabase() {
        assertThat(englishContentDao.fetchByFileName("test_not_existing_file.pdf")).isEmpty();
        // create any record
        englishContentDao.create(new EnglishContent());
        // and try to get not existing again
        assertThat(englishContentDao.fetchByFileName("test_not_existing_file.pdf")).isEmpty();
    }

    @Test
    void testDelete_shouldDeleteRecord_whenRecordExistsInDatabase() {
        // when record exists in DB
        var englishContent = new EnglishContent();
        englishContent.setMetadata(Map.of("key1", "value1", "filename", "test1.pdf"));
        englishContent.setGlossary(Map.of("word1", "description1"));
        var id = englishContentDao.create(englishContent);
        // then delete operation removes one record
        var deletedRecords = englishContentDao.delete(id);
        // delete api returns amount of deleted records
        assertThat(deletedRecords).isEqualTo(1);
        // record created at "when" step has deleted now
        assertThat(englishContentDao.getById(id)).isEmpty();
    }

    @Test
    void testDelete_shouldNotDeleteRecord_whenRecordDoesNotExist() {
        // no records exist in DB
        // then delete operation removes one record
        var deletedRecords = englishContentDao.delete(100501);
        // delete api returns 0 of deleted records
        assertThat(deletedRecords).isEqualTo(0);
    }

}
