package com.library.utils;

import com.library.models.AddBookRequest;
import com.library.models.DeleteBookRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public final class ApiHelper {

    private ApiHelper() {}

    public static Response addBook(AddBookRequest request) {
        return given()
                .body(request)
                .when()
                .post("/Addbook.php");
    }

    public static Response getBook(String bookId) {
        return given()
                .queryParam("ID", bookId)
                .when()
                .get("/GetBook.php");
    }

    public static Response deleteBook(String bookId) {
        return given()
                .body(DeleteBookRequest.builder().id(bookId).build())
                .when()
                .post("/DeleteBook.php");
    }
}
