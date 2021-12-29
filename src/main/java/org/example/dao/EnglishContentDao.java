package org.example.dao;

import org.example.esl.api.EnglishContent;

import java.util.Optional;

public interface EnglishContentDao {

    int create(EnglishContent content);

    Optional<EnglishContent> getById(int id);

}
