package org.example.esl;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static java.util.Optional.empty;
import static org.example.esl.EnglishContentLiterals.ALL_LITERALS;
import static org.example.esl.EnglishContentLiterals.CULTURE_NOTE_KEY;
import static org.example.esl.EnglishContentLiterals.DASH_LITERALS;
import static org.example.esl.EnglishContentLiterals.END_OF_CULTURE_NOTES_LITERALS;
import static org.example.esl.EnglishContentLiterals.GLOSSARY_KEY;
import static org.example.esl.EnglishContentLiterals.NEW_LINE_SYMBOLS_REGEXP;
import static org.example.esl.EnglishContentLiterals.SPACE;
import static org.example.esl.EnglishContentLiterals.SERIAL_SPACES_REGEXP;
import static org.example.esl.EnglishContentLiterals.START_NEW_LINE_LITERALS;
import static org.example.esl.EnglishContentLiterals.START_NEW_LINE_TWICE_LITERALS;
import static org.example.esl.EnglishContentLiterals.WHAT_ELSE_KEY;

/**
 * Totally custom logic of extraction and transformation in straightforward manner.
 * Content transformation is based on structure of ESL document (documents of English as second language podcast)
 */
@Singleton
public class EnglishContentExtractorImpl implements EnglishContentExtractor {

    private Map<String, Optional<String>> extractRawContent(String text, String[][] allLiterals) {
        if (text.isBlank()) {
            return emptyMap();
        }
        var content = new HashMap<String, Optional<String>>();
        int nextSectionStartIndex = 0; //index of position in text
        // i - index of current section (text is going to be split into sections)
        for (int i = 0; i < allLiterals.length; i++) {
            int startSectionIndex = getFirstOccurrence(text, nextSectionStartIndex, allLiterals[i]);
            if (startSectionIndex < 0) {
                content.put(allLiterals[i][0].strip(), empty());
                continue;
            }
            // if next possible index is not found (equals -1) then take endSectionIndex as end of text
            int endSectionIndex = (i < allLiterals.length - 1) ?
                    defineIndexOfNextPossibleSection(text, startSectionIndex, allLiterals, i) : text.length();

            var sectionLettersAmount = allLiterals[i][0].length();
            var contentSection = getContent(text, startSectionIndex + sectionLettersAmount, endSectionIndex);
            content.put(allLiterals[i][0].strip(), Optional.of(contentSection));
            nextSectionStartIndex = endSectionIndex;
        }
        return content;
    }

    private int defineIndexOfNextPossibleSection(String text, int startFrom, String[][] sectionLiterals, int indexOfCurrentSection) {
        int sectionIndex = -1;
        while (sectionIndex == -1 && indexOfCurrentSection < sectionLiterals.length - 1) {
            sectionIndex = getFirstOccurrence(text, startFrom, sectionLiterals[indexOfCurrentSection + 1]);
            indexOfCurrentSection++;
        }
        return sectionIndex;
    }

    public Optional<EnglishContent> extractEscContent(String text) {
        var sections = extractRawContent(text);
        if (sections.isEmpty()) {
            return empty();
        }
        var eslContent = new EnglishContent();
        sections.get(GLOSSARY_KEY).map(this::extractGlossary).ifPresent(eslContent::setGlossary);
        sections.get(WHAT_ELSE_KEY).map(this::extractWhatElse).ifPresent(eslContent::setWhatElse);
        sections.get(CULTURE_NOTE_KEY).map(this::extractCultureNotes).ifPresent(eslContent::setCultureNotes);
        return Optional.of(eslContent);
    }

    /**
     *
     * @param text raw content retrieved from source (in this case from pdf file)
     * @return dictionary contained of raw text split into sections
     */
    public Map<String, Optional<String>> extractRawContent(String text) {
        return extractRawContent(text, ALL_LITERALS);
    }

    private String getContent(String text, int start, int end) {
        return (end <= 0) ? text.substring(start) :  text.substring(start, end);
    }

    /**
     * Extract Glossary section
     * @param text raw text with glossary section
     * @return map of word to its explanation extracted from raw content
     */
    public Map<String, String> extractGlossary(String text) {
        if (text.isBlank()) {
            return emptyMap();
        }
        var wordToDescription = new LinkedHashMap<String, String>();
        int startIndex = 0;
        while (startIndex < text.length()) {
            //find word since start index
            int endOfWord = getFirstOccurrence(text, startIndex, DASH_LITERALS);
            if (endOfWord == -1) {
                break;
            }
            String word = text.substring(startIndex, endOfWord).strip();
            startIndex = endOfWord + " – ".length();
            if (startIndex >= text.length()) {
                //end of the text is reached, nothing left to lose
                break;
            }
            //find next word
            //int endOfNextWord = text.indexOf(" – ", startIndex);
            int endOfNextWord = getFirstOccurrence(text, startIndex, DASH_LITERALS);
            if (endOfNextWord == -1) {
                //this is last word in glossary section. end index is last index of the text
                wordToDescription.put(word, clean(text.substring(startIndex)));
                break;
            }
            int startOfNextWord = getFirstOccurrenceBackward(text, endOfNextWord, START_NEW_LINE_LITERALS);
            //section between two words is definition of first word
            var definition =  clean(text.substring(startIndex, startOfNextWord));
            wordToDescription.put(word, definition);
            startIndex = startOfNextWord;
        }
        return wordToDescription;
    }

    private int getFirstOccurrenceBackward(String text, int startFrom, String[] patterns) {
        int foundPatternIndex = startFrom;
        for (String pattern : patterns) {
            foundPatternIndex = text.lastIndexOf(pattern, startFrom);
            if (foundPatternIndex > - 1) {
                break;
            }
        }
        return foundPatternIndex;
    }

    private int getFirstOccurrence(String text, int startFrom, String[] patterns) {
        int foundPatternIndex = -1;
        for (String pattern : patterns) {
            foundPatternIndex = text.indexOf(pattern, startFrom);
            if (foundPatternIndex > - 1) {
                break;
            }
        }
        return foundPatternIndex;
    }

    public Map<String, String> extractWhatElse(String text) {
        if (text.isBlank()) {
            return emptyMap();
        }
        int currentIndex = 0;
        var wordToDefinition = new LinkedHashMap<String, String>();
        //iterate over the text .currentIndex = getFirstLetter
        while (currentIndex < text.length()) {
            //find word start/end index
            int startOfWord = getFirstLetter(text, currentIndex);
            if (startOfWord == -1) {
                break;
            }
            //startOfWord = startOfWord + 4;
            if (startOfWord >= text.length()) {
                //end of the text is reached, nothing left to do
                break;
            }
            int endOfWord  = getFirstOccurrence(text, startOfWord, START_NEW_LINE_LITERALS);
            if (endOfWord == -1) {
                break;
            }
            //find word explanation end index
            int endOfExplanation = getFirstOccurrence(text, endOfWord, START_NEW_LINE_TWICE_LITERALS);
            if (endOfExplanation == -1) {
                endOfExplanation = text.length();
            }
            //collect word and its explanation
            if (startOfWord < endOfWord && endOfExplanation - endOfWord > 2) {
                var word = text.substring(startOfWord, endOfWord);
                wordToDefinition.put(word.strip(), clean(text.substring(endOfWord, endOfExplanation)));
            }
            currentIndex = endOfExplanation;
        }
        return wordToDefinition;
    }

    private int getFirstLetter(String text, int fromIndex) {
        for (int i = fromIndex ; i < text.length(); i++) {
            if (Character.isLetter(text.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    public String extractCultureNotes(String text) {
        int endOfContent = getFirstOccurrence(text, 0, END_OF_CULTURE_NOTES_LITERALS);
        if (endOfContent == -1) {
            endOfContent = text.length();// > 0 ? text.length() - 1 : 0;
        }
        return clean(text.substring(0, endOfContent));
    }

    private String clean(String str) {
        //replace all new line symbols to one space
        return str.strip().replaceAll(NEW_LINE_SYMBOLS_REGEXP, SPACE).replaceAll(SERIAL_SPACES_REGEXP, SPACE);
    }
}
