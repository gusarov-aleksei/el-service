package org.example.esl;

import org.example.pdf.ResourceLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.esl.EnglishContentLiterals.*;

/**
 * Test with big size text to validate overall algorithm
 * (for ex., cycles, collection of items creation, etc.).
 */
public class EslExtractorContent_FullText_Test implements ResourceLoader {

    final static String TXT_GLOSSARY_IN_TEST_RESOURCE = "txt/glossary.txt";
    final static String TXT_WHAT_ELSE_IN_TEST_RESOURCE = "txt/what_else.txt";
    final static String TXT_FULL_TEXT_IN_TEST_RESOURCE = "txt/full_text.txt";

    @Test
    public void testExtract_whenValidTextIsProvided_shouldExtractMapWithSectionOfTextContent() throws IOException, URISyntaxException {
        var content = readStringFromFile("txt/full_text.txt");

        var ext = new EnglishContentExtractorImpl();
        var res = ext.extractRawContent(content);

        assertThat(res).hasSize(5);
        assertThat(res).containsKey(GLOSSARY_KEY);
        assertThat(res.get(GLOSSARY_KEY)).isNotEmpty();

        assertThat(res).containsKey(COMPREHENSION_QUESTION_KEY);
        assertThat(res.get(COMPREHENSION_QUESTION_KEY)).isNotEmpty();

        assertThat(res).containsKey(CULTURE_NOTE_KEY);
        assertThat(res.get(CULTURE_NOTE_KEY)).isNotEmpty();

        assertThat(res).containsKey(WHAT_ELSE_KEY);
        assertThat(res.get(WHAT_ELSE_KEY)).isNotEmpty();

        assertThat(res).containsKey(COMPLETE_TRANSCRIPT_KEY);
        assertThat(res.get(COMPLETE_TRANSCRIPT_KEY)).isNotEmpty();
    }

    @Test
    public void testExtractGlossaryContent_whenValidTextIsProvided_shouldExtractGlossaryContent() throws IOException, URISyntaxException {
        var glossaryText = readStringFromFile(TXT_GLOSSARY_IN_TEST_RESOURCE);
        var ext = new EnglishContentExtractorImpl();
        var glossary = ext.extractGlossary(glossaryText);

        assertThat(glossary).hasSize(15);
        assertThat(glossary).containsKey("passport");
        assertThat(glossary.get("passport")).contains("a small official book, issued by a national government");

        assertThat(glossary).containsKey("identification / ID");
        assertThat(glossary.get("identification / ID")).contains("cash anyone’s check");

        assertThat(glossary).containsKey("identical");
        assertThat(glossary.get("identical")).contains("I’m speaking with");
    }

    @Test
    public void testExtractWhatElseContent_whenValidTextIsProvided_shouldExtractWhatElseContent() throws IOException, URISyntaxException {
        var content = readStringFromFile(TXT_WHAT_ELSE_IN_TEST_RESOURCE);
        var ext = new EnglishContentExtractorImpl();
        var whatElseSection = ext.extractWhatElse(content);

        assertThat(whatElseSection).hasSize(2);
        assertThat(whatElseSection).containsKey("document");
        assertThat(whatElseSection.get("document")).contains("nuclear power is well documented");

        assertThat(whatElseSection).hasSize(2);
        assertThat(whatElseSection).containsKey("to submit");
        assertThat(whatElseSection.get("to submit")).contains("to show that the man was guilty");
    }

    @Test
    public void testExtractCultureNotesContent_whenValidTextIsProvided_shouldExtractCultureContent() throws IOException, URISyntaxException {
        var content = readStringFromFile(TXT_FULL_TEXT_IN_TEST_RESOURCE);
        var ext = new EnglishContentExtractorImpl();
        var rawCultureNotes = ext.extractRawContent(content).get(CULTURE_NOTE_KEY);

        assertThat(rawCultureNotes).isPresent();

        var cultureNotes = ext.extractCultureNotes(rawCultureNotes.get());

        assertThat(cultureNotes).isNotBlank();
        assertThat(cultureNotes).contains("Department of State");
        assertThat(cultureNotes).contains("information about local crime");
    }

}
