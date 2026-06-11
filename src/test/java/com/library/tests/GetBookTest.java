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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("Library API")
@Feature("Get Book — GET /GetBook.php")
public class GetBookTest extends BaseTest {

    private static String createdBookId;
    private static Response getResponse;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        Response addResp = ApiHelper.addBook(TestData.BOOK_FOR_GET);
        createdBookId = addResp.jsonPath().getString("ID");
        getResponse = ApiHelper.getBook(createdBookId);
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
    @Description("GET /GetBook.php?ID=<valid> must return HTTP 200 OK")
    public void getBook_Returns200() {
        assertThat(getResponse.statusCode())
                .as("Expected HTTP 200 for a valid get-book request")
                .isEqualTo(200);
    }

    @Test
    @Story("b. JSON response structure")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Response is an array; first element must contain book_name, isbn, aisle, author — all String type")
    public void getBook_ResponseHasCorrectStructure() {
        getResponse.then()
                .body("[0]", hasKey("book_name"))
                .body("[0]", hasKey("isbn"))
                .body("[0]", hasKey("aisle"))
                .body("[0]", hasKey("author"))
                .body("[0].book_name", instanceOf(String.class))
                .body("[0].isbn", instanceOf(String.class))
                .body("[0].aisle", instanceOf(String.class))
                .body("[0].author", instanceOf(String.class));
    }

    @Test
    @Story("c. Response time under 500 ms")
    @Severity(SeverityLevel.NORMAL)
    @Description("GET /GetBook.php must respond in less than 500 milliseconds")
    public void getBook_ResponseTimeUnder500ms() {
        long ms = getResponse.timeIn(TimeUnit.MILLISECONDS);
        assertThat(ms)
                .as("Response time (%d ms) must be < %d ms", ms, ApiConfig.MAX_RESPONSE_TIME_MS)
                .isLessThan(ApiConfig.MAX_RESPONSE_TIME_MS);
    }

    @Test
    @Story("d. Non-existent ID returns 404")
    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /GetBook.php with a non-existent ID must return HTTP 404")
    public void getBook_NonExistentId_Returns404() {
        ApiHelper.getBook(TestData.NON_EXISTENT_ID)
                .then()
                .statusCode(404);
    }
}
