package org.example.esl;

import org.example.esl.api.EnglishContent;

import java.util.Map;
import java.util.Optional;

public interface EnglishContentExtractor {

    Optional<EnglishContent> extractEscContent(String text);

    Map<String, Optional<String>> extractRawContent(String text);

    Map<String, String> extractGlossary(String text);

    Map<String, String> extractWhatElse(String text);

    String extractCultureNotes(String text);
}
