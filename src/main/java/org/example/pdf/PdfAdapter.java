package org.example.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * It contains logic of text from pdf extraction adapted for loading text from pdf with particular structure.
 * Non-generalized approach.
 *
 */
public class PdfAdapter {

    private final Logger LOGGER = LoggerFactory.getLogger(PdfAdapter.class);
    //Text region coordinates in pdf layout (to exclude header and bottom)
    public static final float REGION_START_X = 1;
    public static final float REGION_START_Y = 120; //108.48
    public static final float REGION_END_W = 611;
    public static final float REGION_END_H = 601;

    public Optional<String> retrieveTextContent(File file) throws IOException {
        LOGGER.info("Content retrieval for file {}", file);
        try (var doc = PDDocument.load(file)) {
            if (doc.getNumberOfPages() == 0) {
                LOGGER.debug("No pages found in document");
                return Optional.empty();
            }
            StringBuilder sb = new StringBuilder();
            PDFTextStripperByArea pdfTextStripper = getPDFTextStripperByArea();
            for (int i = 0; i < doc.getNumberOfPages(); i++) {
                pdfTextStripper.extractRegions(doc.getPage(i));
                sb.append(pdfTextStripper.getTextForRegion("mainText"));
            }
            return Optional.of(sb.toString());
        }
    }

    public Optional<String> retrieveTextContent(String fileName) throws IOException {
        LOGGER.info("Content retrieval for file {}", fileName);
        return retrieveTextContent(new File(fileName));
    }

    private Rectangle2D createFixedTextRegion() {
        return new Rectangle2D.Double(REGION_START_X, REGION_START_Y, REGION_END_W, REGION_END_H);
    }

    private PDFTextStripperByArea getPDFTextStripperByArea() throws IOException {
        var pdfStripByArea = new PDFTextStripperByArea();
        pdfStripByArea.addRegion("mainText", createFixedTextRegion());
        pdfStripByArea.setSortByPosition(true);
        return pdfStripByArea;
    }
}
