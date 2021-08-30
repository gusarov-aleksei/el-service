package org.example.esl;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
