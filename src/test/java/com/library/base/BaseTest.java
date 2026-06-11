package com.library.base;

import com.library.config.ApiConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeSuite;

import static io.restassured.RestAssured.given;

public class BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void configureRestAssured() {
        RestAssured.baseURI = ApiConfig.BASE_URL;
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .addFilter(new AllureRestAssured())
                .build();

        warmUpApi();
    }

    /**
     * Fires a throwaway request so TLS handshake, DNS resolution and any
     * server-side cold start are not charged to the first measured test.
     */
    private void warmUpApi() {
        try {
            given().get("/GetBook.php?ID=warmup");
        } catch (Exception ignored) {
            // Warm-up is best-effort; real failures surface in the tests.
        }
    }
}
