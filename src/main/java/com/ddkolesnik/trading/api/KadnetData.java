package com.ddkolesnik.trading.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Alexandr Stegnin
 */

@Data
public class KadnetData {

    @JsonProperty("ObjectType")
    private String objectType;

    @JsonProperty("Number")
    private String kadNumber;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Level")
    private String floor;

    @JsonProperty("Area")
    private Double area;

}
