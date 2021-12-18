package org.example.dao;

import org.example.esl.EnglishContent;

import java.util.Optional;

public interface EnglishContentDao {

    int create(EnglishContent content);

    Optional<EnglishContent> getById(int id);

}
