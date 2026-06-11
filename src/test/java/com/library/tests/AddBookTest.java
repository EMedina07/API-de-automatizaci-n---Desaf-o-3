package com.library.tests;

import com.library.base.BaseTest;
import com.library.config.ApiConfig;
import com.library.data.TestData;
import com.library.utils.ApiHelper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("Library API")
@Feature("Add Book — POST /Addbook.php")
public class AddBookTest extends BaseTest {

    private static Response response;
    private static String createdBookId;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        response = ApiHelper.addBook(TestData.VALID_BOOK);
        createdBookId = response.jsonPath().getString("ID");
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        if (createdBookId != null) {
            ApiHelper.deleteBook(createdBookId);
        }
    }

    @Test
    @Story("a. Successful response code")
    @Severity(SeverityLevel.BLOCKER)
    @Description("POST /Addbook.php must return HTTP 200 OK for a valid request")
    public void addBook_Returns200() {
        assertThat(response.statusCode())
                .as("Expected HTTP 200 for a valid add-book request")
                .isEqualTo(200);
    }

    @Test
    @Story("b. JSON response structure")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Response must contain keys 'Msg' (String) and 'ID' (String)")
    public void addBook_ResponseHasCorrectStructure() {
        response.then()
                .body("$", hasKey("Msg"))
                .body("$", hasKey("ID"))
                .body("Msg", instanceOf(String.class))
                .body("ID", instanceOf(String.class));
    }

    @Test
    @Story("c. Response time under 500 ms")
    @Severity(SeverityLevel.NORMAL)
    @Description("POST /Addbook.php must respond in less than 500 milliseconds")
    public void addBook_ResponseTimeUnder500ms() {
        long ms = response.timeIn(TimeUnit.MILLISECONDS);
        assertThat(ms)
                .as("Response time (%d ms) must be < %d ms", ms, ApiConfig.MAX_RESPONSE_TIME_MS)
                .isLessThan(ApiConfig.MAX_RESPONSE_TIME_MS);
    }

    @Test
    @Story("d. ID = isbn + aisle")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The 'ID' field must equal the concatenation of isbn and aisle")
    public void addBook_IdIsIsbnPlusAisle() {
        String expected = TestData.VALID_BOOK.getIsbn() + TestData.VALID_BOOK.getAisle();
        assertThat(response.jsonPath().getString("ID"))
                .as("ID must be isbn(%s) + aisle(%s) = %s",
                        TestData.VALID_BOOK.getIsbn(), TestData.VALID_BOOK.getAisle(), expected)
                .isEqualTo(expected);
    }

    @Test
    @Story("e. Non-numeric aisle returns 500")
    @Severity(SeverityLevel.NORMAL)
    @Description("A non-numeric value in 'aisle' must return HTTP 500")
    public void addBook_NonNumericAisle_Returns500() {
        given()
                .body(TestData.INVALID_AISLE_BOOK)
                .when()
                .post("/Addbook.php")
                .then()
                .statusCode(500);
    }
}
