package org.example.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.response.Response;
import org.assertj.core.util.Strings;
import org.example.pdf.ResourceLoader;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
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
public class FileOperationsResource_Response200_Test implements ResourceLoader {

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

    @Test
    public void testDeleteFiles_whenNonExistingFileNameIsProvided_shouldReturnErrorsInCollection_with200() {
        //initial condition
        var randomName1 = UUID.randomUUID() + ".pdf";
        var randomName2 = UUID.randomUUID() + ".pdf";
        var fileNames = Strings.join(randomName1,randomName2).with(",");
        Response response = given()
                .when()
                .queryParam("fileNames",fileNames)
                .delete("/deleteFiles")
                .andReturn();
        // validate status code and basic body
        // because of bulk deletion, some file could be deleted, some files could be absent at all - for case of absence 200 code is expected too
        response.then()
                .log().ifValidationFails()
                .statusCode(200)
                .body(notNullValue())
                .body("", instanceOf(List.class))
                .body("", hasSize(2))
                .body("get(0).get(\"key\")", is(equalTo(randomName1)))
                .body("get(0).get(\"value\")", is(containsString("NoSuchFileException")))
                .body("get(1).get(\"key\")", is(equalTo(randomName2)))
                .body("get(1).get(\"value\")", is(containsString("NoSuchFileException")));
    }
}
