package org.example.service;

import org.example.esl.api.EnglishContent;
import org.example.esl.EnglishContentExtractor;
import org.example.file.FileOps;
import org.example.pdf.PdfAdapter;
import org.example.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Optional;

/**
 * Class with main logic called from http endpoint or by schedule
 */
@Singleton
public class EnglishContentService implements FileOps {

    private final Logger LOGGER = LoggerFactory.getLogger(EnglishContentService.class);

    @Inject
    SourceConfig config;

    @Inject
    StorageService storageService;

    private final PdfAdapter pdfAdapter = new PdfAdapter();
    @Inject
    EnglishContentExtractor englishContentExtractor;

    /**
     * Returns collection of file names in source directory.
     * That file names are accepted by {@link #extractContent(String fileName) extractContent} method
     * @return array of file names
     */
    public String[] listDocumentNames()  {
        return storageService.listFileNames("", config.extension());
    }

    /**
     * Get extracted and transformed English content from file.
     * @param fileName File name. Or file name and its relative path.
     * @return structured content extracted from pdf file
     */
    public Optional<EnglishContent> extractContent(String fileName) throws IOException {
        LOGGER.info("Content retrieval for file {}", fileName);
        var fileData = storageService.readDataFormFile(fileName);
        var opContent = pdfAdapter.retrieveTextContent(fileData.content)
                .flatMap(englishContentExtractor::extractEscContent);
        opContent.ifPresent(content -> content.setMetadata(fileData.metadata));
        return opContent;
    }
}