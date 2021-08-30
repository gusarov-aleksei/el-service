package org.example.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.example.rest.error.Error.EMPTY_FILE_NAME;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@TestProfile(MockApplicationProfile.class)
public class EnglishContentResource_Response400_Test {

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    public void testExtractEndpoint_shouldReturnBadRequestOfFileName_whenEmptyFileNameProvided() {
        given()
                .when()
                .queryParam("fileName","")
                .get("/extract")
                .then()
                .statusCode(400)
                .body("code", is(EMPTY_FILE_NAME.code))
                .body("message", is(EMPTY_FILE_NAME.message));
    }
    @Test
    public void testExtractEndpoint_shouldReturnBadRequestOfFileName_whenNoFileNameParameterProvided() {
        given()
                .when()
                .get("/extract")
                .then()
                .statusCode(400)
                .body("code", is(EMPTY_FILE_NAME.code))
                .body("message", is(EMPTY_FILE_NAME.message));
    }
}
