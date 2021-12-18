package org.example.dao;

import org.example.esl.EnglishContent;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simple validation for db, jooq and dao. If them work together.
 */
public class EnglishContentDaoIntegrationTest {

    EnglishContentDao englishContentDao = new EnglishContentDaoExample();

    //@Test
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
