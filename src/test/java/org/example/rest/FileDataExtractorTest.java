package org.example.rest;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.util.CaseInsensitiveMap;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static javax.ws.rs.core.HttpHeaders.CONTENT_DISPOSITION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class FileDataExtractorTest implements FileDataExtractor {
    @Test
    public void testRetrieveFileName_shouldReturnOptionalFileName_whenHeaderOfInputPartContainsFileName() {
        var input = mock(InputPart.class);

        var headers = new CaseInsensitiveMap<String>();
        headers.put(CONTENT_DISPOSITION, List.of("form-data; name=\"file\"; filename=\"content.txt\""));
        doReturn(headers).when(input).getHeaders();

        var result = this.retrieveFileName(input);
        assertThat(result).isNotEmpty();
        //not there are no quotes in result
        assertThat(result).hasValue("content.txt");
    }

    @Test
    public void testRetrieveFileName_shouldReturnEmpty_whenNoFileNameInInputPart() {
        var input = mock(InputPart.class);
        var headers = new CaseInsensitiveMap<String>();
        doReturn(headers).when(input).getHeaders();

        //assert when no headers in inputPart
        assertThat(retrieveFileName(input)).isEmpty();
        //assert with CONTENT_DISPOSITION header
        headers.put(CONTENT_DISPOSITION, List.of("some any value"));
        assertThat(retrieveFileName(input)).isEmpty();
    }

    @Test
    public void testRetrieveFileBody_shouldReturnFileBody_whenInputPartRequestHasBody() throws IOException {
        var input = mock(InputPart.class);
        doReturn(new ByteArrayInputStream(new byte[]{1,2,3})).when(input).getBody(InputStream.class, null);

        var result = this.retrieveFileBody(input);
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
    }

    @Test
    public void testRetrieveFiles_shouldReturnFileNameAndItsBody_whenRequestContainsThemInMultiPartStructure() throws IOException {
        var input1 = mockInputPart("form-data; name=\"file\"; filename=\"content1.txt\"",
                new byte[]{1,2,3});
        var input2 = mockInputPart("form-data; name=\"file\"; filename=\"content2.txt\"",
                new byte[]{5,6,7});

        var result = retrieveFiles(List.of(input1, input2));

        assertThat(result).isNotEmpty();
        assertThat(result).hasEntrySatisfying("content1.txt", v -> assertThat(v).containsExactly(1,2,3));
        assertThat(result).hasEntrySatisfying("content2.txt", v -> assertThat(v).containsExactly(5,6,7));

    }

    private InputPart mockInputPart(String contentDispositionHeader, byte[] fileData) throws IOException {
        var input = mock(InputPart.class);
        var headers = new CaseInsensitiveMap<String>();
        headers.put(CONTENT_DISPOSITION, List.of(contentDispositionHeader));
        doReturn(headers).when(input).getHeaders();
        doReturn(new ByteArrayInputStream(fileData)).when(input).getBody(InputStream.class, null);
        return input;
    }

}