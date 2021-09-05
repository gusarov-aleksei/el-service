package org.example.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.example.pdf.ResourceLoader;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

/**
 * Test of REST calls, Quarkus APIs and configurations.
 * Because of working with file system API internally, need to provide environment variable:
 * LOCAL_SOURCE_DIR - local directory for files storing and reading
 *
 * This test validates FileUploadResource endpoint which allows files uploading to LOCAL_SOURCE_DIR
 */
@QuarkusTest
@TestProfile(MockApplicationProfile.class)
public class FileUploadResource_Response200_Test implements ResourceLoader {

    @Test
    public void testUploadMulti_shouldUploadFileToSourceDir() throws URISyntaxException, FileNotFoundException {
        var inputStream = new FileInputStream(getFile("pdf/2 one page.pdf"));
        given().multiPart("file", "2 one page uploaded 2.pdf", inputStream)
                .post("/uploadMulti")
                .then()
                .statusCode(200)
                .body(notNullValue())
                .body("size()", is(1))
                .body("", hasItem("2 one page uploaded 2.pdf"));
    }

    @Test
    public void testUploadMulti_whenTwoFilesAreUploaded_shouldUploadFilesToSourceDir() throws URISyntaxException, FileNotFoundException {
        var input = new FileInputStream(getFile("pdf/2 one page.pdf"));
        var input2 = new FileInputStream(getFile("pdf/4 all pages.pdf"));
        given().multiPart("file", "2 one page uploaded 3.pdf", input)
                .multiPart("file", "4 all pages uploaded 4.pdf", input2)
                .post("/uploadMulti")
                .then()
                .statusCode(200)
                .body(notNullValue())
                .body("size()", is(2))
                .body("", hasItem("2 one page uploaded 3.pdf"))
                .body("", hasItem("4 all pages uploaded 4.pdf"));
    }

    @Test
    public void testUpload_shouldUploadFileToSourceDir() throws URISyntaxException {
        var file = getFile("pdf/2 one page.pdf");
        given()
                .multiPart("file", file)
                .multiPart("fileName", "2 one page uploaded.pdf")
                .post("/upload")
                .then()
                .statusCode(200)
                .body(notNullValue())
                .body(is("2 one page uploaded.pdf"));
    }

    @Test
    public void testUploadMulti_whenNoFileNamePassed_shouldReturnResponseWithoutFileNames_with200() throws URISyntaxException, FileNotFoundException {
        //one file name (or file content of bytes) is not passed but another file name can be passed and saved
        //in this case 200 is returned but there is no file name in response
        var input = new FileInputStream(getFile("pdf/2 one page.pdf"));
        var input2 = new FileInputStream(getFile("pdf/4 all pages.pdf"));
        given().multiPart("file", null, input)
                .multiPart("file", "4 all pages uploaded 5.pdf", input2)
                .post("/uploadMulti")
                .then()
                .statusCode(200)
                .body(notNullValue())
                .body("size()", is(1))
                .body("", hasItem("4 all pages uploaded 5.pdf"));
    }
}
