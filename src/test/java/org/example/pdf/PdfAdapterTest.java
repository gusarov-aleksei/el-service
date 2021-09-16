package org.example.pdf;

import org.example.file.FileOps;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.esl.EnglishContentLiterals.COMPLETE_TRANSCRIPT_KEY;
import static org.example.esl.EnglishContentLiterals.COMPREHENSION_QUESTION_KEY;
import static org.example.esl.EnglishContentLiterals.CULTURE_NOTE_KEY;
import static org.example.esl.EnglishContentLiterals.GLOSSARY_KEY;
import static org.example.esl.EnglishContentLiterals.WHAT_ELSE_KEY;

public class PdfAdapterTest implements ResourceLoader, FileOps {

    final static String EMPTY_PDF = "pdf/0 empty.pdf";
    final static String EMPTY_ONE_PAGE_PDF = "pdf/1 one page.pdf";
    final static String NON_EMPTY_ONE_PAGE_PDF = "pdf/2 one page.pdf";
    //final static String VALID_PDF = "pdf/ESL Podcast 1080 – Automating Production .pdf";
    final static String VALID_PDF = "pdf/4 all pages.pdf";

    final static String ABSOLUTE_PATH_TO_BASE_DIR =
            PdfAdapterTest.class.getClassLoader().getResource("").getFile();

    PdfAdapter pdfAdapter = new PdfAdapter();

    /**
     * Test validates text retrieval from pdf file.
     * @throws IOException file reading throws exception
     */
    @Test
    public void testReadPdf_shouldReadPdf() throws IOException {
        var pdfFileData = readFromFile(ABSOLUTE_PATH_TO_BASE_DIR + VALID_PDF);
        var textContent = pdfAdapter.retrieveTextContent(pdfFileData);
        assertThat(textContent).isNotEmpty();
        assertThat(textContent.get()).isNotBlank();
        //assert some internal key tokens are in text for pdf with concrete content
        assertThat(textContent.get()).contains(GLOSSARY_KEY);
        assertThat(textContent.get()).contains(COMPREHENSION_QUESTION_KEY);
        assertThat(textContent.get()).contains(WHAT_ELSE_KEY);
        assertThat(textContent.get()).contains(CULTURE_NOTE_KEY);
        assertThat(textContent.get()).contains(COMPLETE_TRANSCRIPT_KEY);
    }

    /**
     * Test validates text retrieval from pdf file.
     */
    @Test
    public void testReadPdf_shouldRetrieveEmpty_whePdfFileIsEmptyAtAll_1() throws IOException {
        var pdfFileData = readFromFile(ABSOLUTE_PATH_TO_BASE_DIR + EMPTY_PDF);
        var textContent = pdfAdapter.retrieveTextContent(pdfFileData);
        assertThat(textContent).isEmpty();
    }

    /**
     * Test validates text retrieval from pdf file.
     */
    @Test
    public void testReadPdf_shouldRetrieveEmptyString_whePdfFileIsEmptyAtAll_2() throws IOException {
        var pdfFileData = readFromFile(ABSOLUTE_PATH_TO_BASE_DIR + EMPTY_ONE_PAGE_PDF);
        var textContent = pdfAdapter.retrieveTextContent(pdfFileData);
        assertThat(textContent).isNotEmpty();
        assertThat(textContent.get()).isBlank();
    }

    @Test
    public void testReadPdf_shouldRetrieveEmptyString_whePdfFileIsEmptyAtAll_3() throws IOException {
        var pdfFileData = readFromFile(ABSOLUTE_PATH_TO_BASE_DIR + NON_EMPTY_ONE_PAGE_PDF);
        var textContent = pdfAdapter.retrieveTextContent(pdfFileData);
        assertThat(textContent).isNotEmpty();
        assertThat(textContent.get()).contains("GLOSSARY");
        assertThat(textContent.get()).contains("word 1", "word 2");
    }
}
