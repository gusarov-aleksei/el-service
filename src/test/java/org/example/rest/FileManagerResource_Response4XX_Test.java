package org.example.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.response.Response;
import org.example.pdf.ResourceLoader;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

@QuarkusTest
@TestProfile(MockApplicationProfile.class)
public class FileManagerResource_Response4XX_Test implements ResourceLoader {
    //415 - Unsupported Media Type
    @Test
    public void testUploadMulti_whenNoContentPassed_shouldReturn415() {
        given()
                .post("/uploadMulti")
                .then()
                .statusCode(415);
    }

    @Test
    public void testUpload_whenNoContentPassed_shouldReturn415() {
        given()
                .post("/upload")
                .then()
                .statusCode(415);
    }

    @Test
    public void testUpload_whenNoFileNamePassed_shouldReturn400() throws URISyntaxException {
        //don't call .multiPart("fileName", "random name 1.pdf")
        given().multiPart("file", getFile("pdf/2 one page.pdf"))
                .post("/upload")
                .then()
                .statusCode(400)
                .body(notNullValue())
                .body("code", is("ETL-0001"))
                .body("message", is("Request parameter 'fileName' must be not empty"));
    }

    @Test
    public void testUpload_whenEmptyBytesContentIsPassed_shouldReturn400() {
        //don't call .multiPart("file", getFile("pdf/2 one page.pdf"))
        given().multiPart("fileName", "random name 1.pdf")
                .post("/upload")
                .then()
                .statusCode(400)
                .body(notNullValue())
                .body("code", is("ETL-0004"))
                .body("message", is("Request parameter 'file' must be not empty"));
    }

    @Test
    public void testDeleteFiles_whenEmptyFileNamesProvided_shouldReturn400() {
        Response response = given()
                .when()
                .queryParam("fileNames", "")
                .delete("/deleteFiles")
                .andReturn();
        // validate status code and basic body
        response.then()
                .log().ifValidationFails()
                .statusCode(400)
                .body(notNullValue())
                .body("code", is("ETL-0001"))
                .body("message", is("Request parameter 'fileName' must be not empty"));
    }

    @Test
    public void testDeleteFiles_whenFileNamesParameterIsNotProvided_shouldReturn400() {
        Response response = given()
                .when()
                .delete("/deleteFiles")
                .andReturn();
        // validate status code and basic body
        response.then()
                .log().ifValidationFails()
                .statusCode(400)
                .body(notNullValue())
                .body("code", is("ETL-0001"))
                .body("message", is("Request parameter 'fileName' must be not empty"));
    }
}
