package org.example.testing;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.example.esl.EnglishContent;

import java.io.IOException;

import static org.example.pdf.PdfAdapter.REGION_END_H;
import static org.example.pdf.PdfAdapter.REGION_END_W;
import static org.example.pdf.PdfAdapter.REGION_START_X;
import static org.example.pdf.PdfAdapter.REGION_START_Y;

/**
 * Experimental class for pdf file with test content generation
 */
public class PdfDataGenerator {

    public static final int SECTION_INTERVAL_HEIGHT = 20;
    public static final int SECTION_FONT_SIZE = 12;
    public static final int INTERLINE_HEIGHT = 15;
    public static final int TEXT_FONT_SIZE = 10;


    public void createEmptyDocument(String fullName) throws IOException {
        try (var document = new PDDocument()) {
            document.save(fullName);
        }
    }

    public void createDocumentWithOnePage(String fullName) throws IOException {
        try (var document = new PDDocument()) {
            document.addPage(new PDPage());
            document.save(fullName);
        }
    }

    public void createDocumentWithOnePageAndContent(String fullName, EnglishContent eslContent) throws IOException {
        try (var document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            // Start a new content stream which will "hold" the to be created content
            try (var stream = new PDPageContentStream(document, page)) {
                stream.addRect(REGION_START_X, REGION_START_Y, REGION_END_W, REGION_END_H);
                stream.beginText();
                addSectionTitle(stream, "GLOSSARY");
                addGlossary(stream, eslContent);
                stream.endText();
            }
            document.save(fullName);
        }
    }

    private void addSectionTitle(PDPageContentStream stream, String title) throws IOException {
        stream.setFont(PDType1Font.HELVETICA_BOLD, SECTION_FONT_SIZE);
        stream.newLineAtOffset( 100, 670 );
        stream.showText(title);
        stream.newLineAtOffset(0, -SECTION_INTERVAL_HEIGHT);
        stream.newLineAtOffset(0, -SECTION_INTERVAL_HEIGHT);
    }

    private void addGlossary(PDPageContentStream stream, EnglishContent content) throws IOException {
        for (var entry : content.getGlossary().entrySet()) {
            stream.setFont(PDType1Font.HELVETICA_BOLD, TEXT_FONT_SIZE);
            stream.showText(entry.getKey());
            stream.setFont(PDType1Font.TIMES_ROMAN, TEXT_FONT_SIZE);
            stream.showText(" – " + entry.getValue());
            stream.newLineAtOffset(0, -INTERLINE_HEIGHT);
        }
    }
 }
