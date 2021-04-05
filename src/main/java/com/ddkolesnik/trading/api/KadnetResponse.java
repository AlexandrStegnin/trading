package com.ddkolesnik.trading.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Data
public class KadnetResponse {

    @JsonProperty("Result")
    private Boolean result;

    @JsonProperty("Data")
    private List<KadnetData> data;

}
