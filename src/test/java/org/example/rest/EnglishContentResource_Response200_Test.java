package org.example.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasValue;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

/**
 * Test of REST calls, Quarkus APIs and configurations.
 * Because of working with file system API internally, need to provide environment variable:
 * LOCAL_SOURCE_DIR - local directory for files storing and reading
 */
@QuarkusTest
@TestProfile(MockApplicationProfile.class)
public class EnglishContentResource_Response200_Test {
    @Disabled
    @Test
    public void testExtractEndpoint_shouldReturnTextStructure_whenValidFileNameIsProvided() {
        given()
                .when()
                .queryParam("fileName","ESL Podcast 1015 - Conducting a Search.pdf")
                .get("/extract")
                .then()
                .statusCode(200)
                .body("cultureNotes", startsWith("The FBI Victims Identification"))
                .body("cultureNotes", endsWith("(not share with others)."))
                .body("glossary", hasKey("security chief"))
                .body("glossary", hasValue(startsWith("a person whose job")))
                .body("glossary", hasValue(endsWith("in the air and to be quiet.")))
                .body("glossary", hasKey("Taser"))
                .body("glossary", hasValue(startsWith("a weapon, often used by police")))
                .body("glossary", hasValue(endsWith("not  dangerous or threatening.")))
                .body("whatElse", hasKey("to report in"))
                .body("whatElse", hasKey(startsWith("don't look now")));
    }
    @Test
    public void testExtractEndpoint_sample_file_shouldReturnTextStructure_whenValidFileNameIsProvided() {
        given()
                .when()
                .queryParam("fileName","4 all pages.pdf")
                .get("/extract")
                .then()
                .statusCode(200)
                .body("cultureNotes", startsWith("Why Do We Carry on Smoking"))
                .body("cultureNotes", endsWith("justify their habit."))
                .body("glossary", hasKey("EASYWAY"))
                .body("glossary", hasValue(startsWith("Easy way to stop smoking")))
                .body("glossary", hasKey("Statement about mind"))
                .body("glossary", hasValue(startsWith("It took me a long time")))
                .body("glossary", hasValue(endsWith("It was all in the mind.")))
                .body("glossary", hasKey("About enjoyment"))
                .body("glossary", hasValue(startsWith("Some say cigarettes are very enjoyable. They aren't.")))
                .body("glossary", hasValue(endsWith("Enjoyment has nothing to do with it.")))
                .body("whatElse", hasKey("Habits context"))
                .body("whatElse", hasKey(startsWith("On reason of smoking")));
    }

    @Test
    public void testListEndpoint_shouldReturnListOfFiles_whenLocalDirectoryContainsFiles() {
        given()
                .when()
                .get("/list")
                .then()
                .statusCode(200)
                .body(notNullValue())
                .body("size()", greaterThan(0))
                .body("", hasItem(endsWith(".pdf")))
                .body("get(0)", endsWith(".pdf"));
                //some Groovy script is placed in body for getting data for assertion
    }
}
