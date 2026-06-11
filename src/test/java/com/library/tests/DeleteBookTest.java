package com.library.tests;

import com.library.base.BaseTest;
import com.library.config.ApiConfig;
import com.library.data.TestData;
import com.library.utils.ApiHelper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("Library API")
@Feature("Delete Book — POST /DeleteBook.php")
public class DeleteBookTest extends BaseTest {

    private static Response deleteResponse;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        Response addResp = ApiHelper.addBook(TestData.BOOK_FOR_DELETE);
        String bookId = addResp.jsonPath().getString("ID");
        deleteResponse = ApiHelper.deleteBook(bookId);
    }

    @Test
    @Story("a. Successful response code")
    @Severity(SeverityLevel.BLOCKER)
    @Description("POST /DeleteBook.php must return HTTP 200 OK for an existing book")
    public void deleteBook_Returns200() {
        assertThat(deleteResponse.statusCode())
                .as("Expected HTTP 200 for a valid delete-book request")
                .isEqualTo(200);
    }

    @Test
    @Story("b. JSON response structure")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Response must contain key 'msg' of type String")
    public void deleteBook_ResponseHasCorrectStructure() {
        deleteResponse.then()
                .body("$", hasKey("msg"))
                .body("msg", instanceOf(String.class));
    }

    @Test
    @Story("c. Response time under 500 ms")
    @Severity(SeverityLevel.NORMAL)
    @Description("POST /DeleteBook.php must respond in less than 500 milliseconds")
    public void deleteBook_ResponseTimeUnder500ms() {
        long ms = deleteResponse.timeIn(TimeUnit.MILLISECONDS);
        assertThat(ms)
                .as("Response time (%d ms) must be < %d ms", ms, ApiConfig.MAX_RESPONSE_TIME_MS)
                .isLessThan(ApiConfig.MAX_RESPONSE_TIME_MS);
    }

    @Test
    @Story("d. Non-existent ID returns 404")
    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /DeleteBook.php with a non-existent ID must return HTTP 404")
    public void deleteBook_NonExistentId_Returns404() {
        ApiHelper.deleteBook(TestData.NON_EXISTENT_ID)
                .then()
                .statusCode(404);
    }
}
