package org.example.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.example.rest.error.Error.FILE_NOT_FOUND;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@TestProfile(MockApplicationProfile.class)
public class EnglishContentResource_Response404_Test {

    @Test
    public void testExtractEndpoint_shouldReturnNotFound_whenNonExistingFileNameProvided() {
        given()
                .when()
                .queryParam("fileName","not_existing_file")
                .get("/extract")
                .then()
                .statusCode(404)
                .body("code", is(FILE_NOT_FOUND.code))
                .body("message", is(FILE_NOT_FOUND.message));
    }

}
