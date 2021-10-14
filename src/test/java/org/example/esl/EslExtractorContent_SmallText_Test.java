package org.example.esl;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.esl.EnglishContentLiterals.DASH_1;
import static org.example.esl.EnglishContentLiterals.DASH_2;

/**
 * Test with small input text. To validate separately parts of extract algorithm.
 */
public class EslExtractorContent_SmallText_Test {

    EnglishContentExtractorImpl englishContentExtractorImpl = new EnglishContentExtractorImpl();

    @Test
    public void testExtractWhatElse_shouldExtractWhatElseContent_whenInputStringHasValidSequence() {
        var text = "\r\n\r\n word 1\r\nSome explanation 1\r\n\r\nanother word 2\r\nAnother explanation 2";

        var result = englishContentExtractorImpl.extractWhatElse(text);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get("word 1")).isEqualTo("Some explanation 1");
        assertThat(result.get("another word 2")).isEqualTo("Another explanation 2");
    }

    @Test
    public void testExtractWhatElse_shouldExtractWhatElseContent_whenInputStringHasValidSequence_withUnixLineSeparator() {
        var text = "\n\n word 1\nSome explanation 1\n\nanother word 2\nAnother explanation 2";

        var result = englishContentExtractorImpl.extractWhatElse(text);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get("word 1")).isEqualTo("Some explanation 1");
        assertThat(result.get("another word 2")).isEqualTo("Another explanation 2");
    }

    @Test
    public void testExtractGlossary_shouldExtractGlossaryContent_whenInputStringHasValidSequence() {
        var text = "statement 1"+DASH_1+"explanation of statement \r\n new line of explanation \r\n"
                + "statement 2"+DASH_2+"another explanation \r\n and another one \r\n";

        var result = englishContentExtractorImpl.extractGlossary(text);

        assertThat(result).isNotNull().hasSize(2);
        assertThat(result.get("statement 1")).contains("explanation of statement").contains("new line of explanation");
        assertThat(result.get("statement 2")).contains("another explanation").contains("and another one");
    }

    @Test
    public void testExtractGlossary_shouldExtractGlossaryContent_whenInputStringHasValidSequence_2() {
        // another input data with sequence of \r\n\r\n\r\n
        var text = "statement 1"+DASH_1+"explanation of statement \r\n new line of explanation \r\n\r\n\r\n \r\n\r\n \r\n"
                + "statement 2"+DASH_2+"another explanation \r\n and another one \r\n \r\n";

        var result = englishContentExtractorImpl.extractGlossary(text);

        assertThat(result).isNotNull().hasSize(2);
        assertThat(result.get("statement 1")).contains("explanation of statement").contains("new line of explanation");
        assertThat(result.get("statement 2")).contains("another explanation").contains("and another one");
    }

    @Test
    public void testExtractGlossary_shouldExtractGlossaryContent_whenInputStringHasValidSequence_withUnixLineSeparator() {
        var text = "statement 1"+DASH_1+"explanation of statement \n new line of explanation \n"
                + "statement 2"+DASH_2+"another explanation \n and another one \n";

        var result = englishContentExtractorImpl.extractGlossary(text);

        assertThat(result).isNotNull().hasSize(2);
        assertThat(result.get("statement 1")).contains("explanation of statement").contains("new line of explanation");
        assertThat(result.get("statement 2")).contains("another explanation").contains("and another one");
    }

    @Test
    public void testExtractGlossary_shouldExtractGlossaryContent_whenInputStringHasValidSequence_withUnixLineSeparator_2() {
        //another input data with sequence of \n\n\n
        var text = "statement 1"+DASH_1+"explanation of statement \n new line of explanation \n\n\n \n \n\n"
                + "statement 2"+DASH_2+"another explanation \n and another one \n \n \n";

        var result = englishContentExtractorImpl.extractGlossary(text);

        assertThat(result).isNotNull().hasSize(2);
        assertThat(result.get("statement 1")).contains("explanation of statement").contains("new line of explanation");
        assertThat(result.get("statement 2")).contains("another explanation").contains("and another one");
    }

    @Test
    public void testExtractWhatElse_shouldExtractWhatElseContent_whenInputStringIsValid_2() {
        var text = "\r\n\r\n word 1\r\nSome explanation 1\r\n \r\nanother word 2\r\nAnother explanation 2\r\n \r\n";

        var result = englishContentExtractorImpl.extractWhatElse(text);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get("word 1")).isEqualTo("Some explanation 1");
        assertThat(result.get("another word 2")).isEqualTo("Another explanation 2");
    }

    @Test
    public void testExtractWhatElse_shouldReturnEmptyMap_whenInputStringIsEmpty() {
        var text = "";

        var result = englishContentExtractorImpl.extractWhatElse(text);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    public void testExtractWhatElse_shouldReturnEmptyMap_whenTextHasNoPatterns() {
        var text = "This text is without any defined pattern. Situation is possible.";

        var result = englishContentExtractorImpl.extractWhatElse(text);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }
}
