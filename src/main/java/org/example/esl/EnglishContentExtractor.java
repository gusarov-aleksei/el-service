package org.example.esl;

import java.util.Map;
import java.util.Optional;

public interface EnglishContentExtractor {

    Optional<EnglishContent> extractEscContent(String text);

    Map<String, Optional<String>> extractRawContent(String text);

    Map<String, String> extractGlossary(String rawTextWithGlossary);

    Map<String, String> extractWhatElse(String text);

    String extractCultureNotes(String text);
}
