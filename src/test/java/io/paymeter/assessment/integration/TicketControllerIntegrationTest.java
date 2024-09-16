package io.paymeter.assessment.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TicketControllerIntegrationTest {

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void testCalculatePrice_Success() {
        String requestBody = """
                {
                    "parkingId": "P000123",
                    "from": "2023-09-15T10:00:00",
                    "to": "2023-09-15T14:00:00"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/tickets/calculate")
                .then()
                .statusCode(200)
                .body("parkingId", equalTo("P000123"))
                .body("duration", equalTo(240))
                .body("price", equalTo("800EUR"));
    }

    @Test
    void testCalculatePrice_PriceShouldBeZeroWhenStayedLessThanOneMinute() {
        String requestBody = """
                {
                    "parkingId": "P000123",
                    "from": "2023-09-15T10:00:00",
                    "to": "2023-09-15T10:00:50"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/tickets/calculate")
                .then()
                .statusCode(200)
                .body("parkingId", equalTo("P000123"))
                .body("duration", equalTo(0))
                .body("price", equalTo("0EUR"));
    }

    @Test
    void testCalculatePrice_ShouldApplyMaxPricePerDay() {
        String requestBody = """
                {
                    "parkingId": "P000123",
                    "from": "2023-09-15T10:00:00",
                    "to": "2023-09-16T10:00:00"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/tickets/calculate")
                .then()
                .statusCode(200)
                .body("parkingId", equalTo("P000123"))
                .body("duration", equalTo(1440))
                .body("price", equalTo("1500EUR"));
    }

    @Test
    void testCalculatePrice_ShouldApplyMaxPriceEveryTwelveHoursDiscount() {
        String requestBody = """
                {
                    "parkingId": "P000456",
                    "from": "2023-09-15T10:00:00",
                    "to": "2023-09-15T22:00:00"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/tickets/calculate")
                .then()
                .statusCode(200)
                .body("parkingId", equalTo("P000456"))
                .body("duration", equalTo(720))
                .body("price", equalTo("2000EUR"));
    }

    @Test
    void testCalculatePrice_ParkingNotFound() {
        String requestBody = """
                {
                    "parkingId": "P000999",
                    "from": "2023-09-15T10:00:00",
                    "to": "2023-09-15T14:00:00"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/tickets/calculate")
                .then()
                .statusCode(404)
                .body(equalTo("Parking with ID 'P000999' not found."));
    }

    @Test
    void testCalculatePrice_InvalidDateFormatRequest() {
        String invalidRequestBody = """
                {
                    "parkingId": "P000123",
                    "from": "invalid-date-format"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(invalidRequestBody)
                .when()
                .post("/tickets/calculate")
                .then()
                .statusCode(400)
                .body(equalTo("Invalid date format. Please use ISO 8601 format."));
    }

    @Test
    void testCalculatePrice_NullParkingIdRequest() {
        String invalidRequestBody = """
                {
                    "from": "2023-09-15T10:00:00"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(invalidRequestBody)
                .when()
                .post("/tickets/calculate")
                .then()
                .statusCode(400)
                .body("parkingId", equalTo("Parking ID is required"));
    }

    @Test
    void testCalculatePrice_ToDateShouldNotBeBeforeFromDate() {
        String invalidRequestBody = """
                {
                  "parkingId": "P000123",
                  "from": "2024-09-15T08:00:00",
                  "to": "2024-09-14T21:00:00"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(invalidRequestBody)
                .when()
                .post("/tickets/calculate")
                .then()
                .statusCode(400)
                .body(equalTo("The 'to' date must not be before the 'from' date."));
    }
}
