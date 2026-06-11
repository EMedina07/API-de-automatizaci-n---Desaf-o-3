package com.library.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddBookResponse {

    @JsonProperty("Msg")
    private String msg;

    @JsonProperty("ID")
    private String id;
}
