package com.library.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetBookResponse {

    @JsonProperty("book_name")
    private String bookName;

    private String isbn;
    private String aisle;
    private String author;
}
