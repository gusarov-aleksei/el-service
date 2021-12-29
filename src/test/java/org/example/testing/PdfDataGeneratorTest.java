package org.example.testing;

import org.example.esl.api.EnglishContent;
import org.example.pdf.ResourceLoader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Test for samples data generation.
 * It generates base pdf files with base structure which are used in unit-tests.
 */
@Disabled
public class PdfDataGeneratorTest implements ResourceLoader {

    final static String PATH_FOR_FILE = "pdf/";

    final static String DIR_ABSOLUTE_PATH =
            PdfDataGeneratorTest.class.getClassLoader().getResource(PATH_FOR_FILE).getFile();
    final static String FILE_ABSOLUTE_PATH_2 = DIR_ABSOLUTE_PATH + "2 one page.pdf";

    PdfDataGenerator dataGenerator = new PdfDataGenerator();

    @Test
    public void testGenerateEmptyFile() throws IOException {
        //var pathToData = absolutPathToDirectory(PATH_FOR_FILE);
        dataGenerator.createEmptyDocument(DIR_ABSOLUTE_PATH + "0 empty.pdf");
    }

    @Test
    public void testGenerateFileWithEmptyPage() throws IOException {
        //var pathToData = absolutPathToDirectory(PATH_FOR_FILE);
        dataGenerator.createDocumentWithOnePage(DIR_ABSOLUTE_PATH + "1 one page.pdf");
    }

    @Test
    public void testGenerateFileWithSomeContent() throws IOException {
        var content = new EnglishContent();
        var glossary = new LinkedHashMap<String, String>();
        glossary.put("EASYWAY", "Easy way to stop smoking");
        glossary.put("statement 1","I don't expect you to believe me at this stage, " +
                "but by the time you have finished the book, you will understand.");
        glossary.put("statement 2","If when you finish the book, you feel that <br> " +
                "you owe me a debt of gratitude, you can more than repay that debt. <br>" +
                "Not just by recommending EASYWAY to your friends, but whenever you see <br>" +
                " a TV or radio programme, or read a newspaper article advocating <br>" +
                "some other method, write to them or phone them asking why they aren't <br>" +
                "advocating EASYWAY.");
        content.setGlossary(glossary);
        dataGenerator.createDocumentWithOnePageAndContent(FILE_ABSOLUTE_PATH_2, content);
    }
}
