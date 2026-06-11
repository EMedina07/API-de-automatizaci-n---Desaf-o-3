package com.library.tests;

import com.library.base.BaseTest;
import com.library.config.ApiConfig;
import com.library.data.TestData;
import com.library.utils.ApiHelper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("Library API")
@Feature("Integration — Full CRUD Flow")
public class LibraryIntegrationTest extends BaseTest {

    private static String bookId1;
    private static String bookId2;

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        if (bookId1 != null) ApiHelper.deleteBook(bookId1);
        if (bookId2 != null) ApiHelper.deleteBook(bookId2);
    }

    // ─── i. Create both books ────────────────────────────────────────────────

    @Test(priority = 1)
    @Story("i. Create 2 books — validates a, b, c, d")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Creates 2 books and validates status 200, JSON structure, response time < 500ms, and ID = isbn+aisle")
    public void integration_CreateBothBooks() {
        Response resp1 = ApiHelper.addBook(TestData.INTEGRATION_BOOK_1);
        assertAddResponse(resp1,
                TestData.INTEGRATION_BOOK_1.getIsbn(),
                TestData.INTEGRATION_BOOK_1.getAisle());
        bookId1 = resp1.jsonPath().getString("ID");

        Response resp2 = ApiHelper.addBook(TestData.INTEGRATION_BOOK_2);
        assertAddResponse(resp2,
                TestData.INTEGRATION_BOOK_2.getIsbn(),
                TestData.INTEGRATION_BOOK_2.getAisle());
        bookId2 = resp2.jsonPath().getString("ID");
    }

    // ─── ii. Get both books ──────────────────────────────────────────────────

    @Test(priority = 2, dependsOnMethods = "integration_CreateBothBooks")
    @Story("ii. Get both books — validates a, b, c")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Retrieves both books and validates status 200, JSON structure, and response time < 500ms")
    public void integration_GetBothBooks() {
        assertGetResponse(ApiHelper.getBook(bookId1));
        assertGetResponse(ApiHelper.getBook(bookId2));
    }

    // ─── iii. Delete both books ──────────────────────────────────────────────

    @Test(priority = 3, dependsOnMethods = "integration_GetBothBooks")
    @Story("iii. Delete both books — validates a, b, c")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Deletes both books and validates status 200, JSON structure, and response time < 500ms")
    public void integration_DeleteBothBooks() {
        assertDeleteResponse(ApiHelper.deleteBook(bookId1));
        bookId1 = null;

        assertDeleteResponse(ApiHelper.deleteBook(bookId2));
        bookId2 = null;
    }

    // ─── iv. Verify books are gone ───────────────────────────────────────────

    @Test(priority = 4, dependsOnMethods = "integration_DeleteBothBooks")
    @Story("iv. Verify deleted books return 404 — validates d")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Querying the deleted books must return HTTP 404 (book no longer exists)")
    public void integration_VerifyDeletedBooksNotFound() {
        String id1 = TestData.INTEGRATION_BOOK_1.getIsbn() + TestData.INTEGRATION_BOOK_1.getAisle();
        String id2 = TestData.INTEGRATION_BOOK_2.getIsbn() + TestData.INTEGRATION_BOOK_2.getAisle();

        ApiHelper.getBook(id1).then()
                .statusCode(404);
        ApiHelper.getBook(id2).then()
                .statusCode(404);
    }

    // ─── Assertion helpers ────────────────────────────────────────────────────

    private void assertAddResponse(Response resp, String isbn, String aisle) {
        assertThat(resp.statusCode()).isEqualTo(200);
        resp.then()
                .body("$", hasKey("Msg"))
                .body("$", hasKey("ID"))
                .body("Msg", instanceOf(String.class))
                .body("ID", instanceOf(String.class));
        assertThat(resp.timeIn(TimeUnit.MILLISECONDS)).isLessThan(ApiConfig.MAX_RESPONSE_TIME_MS);
        assertThat(resp.jsonPath().getString("ID")).isEqualTo(isbn + aisle);
    }

    private void assertGetResponse(Response resp) {
        assertThat(resp.statusCode()).isEqualTo(200);
        resp.then()
                .body("[0]", hasKey("book_name"))
                .body("[0]", hasKey("isbn"))
                .body("[0]", hasKey("aisle"))
                .body("[0]", hasKey("author"))
                .body("[0].book_name", instanceOf(String.class))
                .body("[0].isbn", instanceOf(String.class))
                .body("[0].aisle", instanceOf(String.class))
                .body("[0].author", instanceOf(String.class));
        assertThat(resp.timeIn(TimeUnit.MILLISECONDS)).isLessThan(ApiConfig.MAX_RESPONSE_TIME_MS);
    }

    private void assertDeleteResponse(Response resp) {
        assertThat(resp.statusCode()).isEqualTo(200);
        resp.then()
                .body("$", hasKey("msg"))
                .body("msg", instanceOf(String.class));
        assertThat(resp.timeIn(TimeUnit.MILLISECONDS)).isLessThan(ApiConfig.MAX_RESPONSE_TIME_MS);
    }
}
