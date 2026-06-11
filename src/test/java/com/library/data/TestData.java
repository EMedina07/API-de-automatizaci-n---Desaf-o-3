package com.library.data;

import com.library.models.AddBookRequest;

public final class TestData {

    private TestData() {}

    public static final AddBookRequest VALID_BOOK = AddBookRequest.builder()
            .name("Clean Code")
            .isbn("CC")
            .aisle("9901")
            .author("Robert C. Martin")
            .build();

    public static final AddBookRequest INVALID_AISLE_BOOK = AddBookRequest.builder()
            .name("Invalid Aisle Book")
            .isbn("INV")
            .aisle("xyz")
            .author("Test Author")
            .build();

    public static final AddBookRequest BOOK_FOR_GET = AddBookRequest.builder()
            .name("The Pragmatic Programmer")
            .isbn("TPP")
            .aisle("9941")
            .author("David Thomas")
            .build();

    public static final AddBookRequest BOOK_FOR_DELETE = AddBookRequest.builder()
            .name("Refactoring")
            .isbn("REF")
            .aisle("9931")
            .author("Martin Fowler")
            .build();

    public static final AddBookRequest INTEGRATION_BOOK_1 = AddBookRequest.builder()
            .name("Design Patterns")
            .isbn("DP")
            .aisle("9911")
            .author("Erich Gamma")
            .build();

    public static final AddBookRequest INTEGRATION_BOOK_2 = AddBookRequest.builder()
            .name("Working Effectively with Legacy Code")
            .isbn("WE")
            .aisle("9921")
            .author("Michael Feathers")
            .build();

    public static final String NON_EXISTENT_ID = "NONEXISTENT00000";
}
