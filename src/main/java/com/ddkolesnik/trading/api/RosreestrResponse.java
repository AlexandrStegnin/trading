package com.ddkolesnik.trading.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Alexandr Stegnin
 */

@Data
public class RosreestrResponse {

    @JsonProperty("objects")
    private RosreestrObject object;

    private boolean grouped;

    private String mode;

    private int found;

    @JsonProperty("found_all")
    private boolean foundAll;

    private String region;

    @JsonProperty("error")
    private Object error;

}
