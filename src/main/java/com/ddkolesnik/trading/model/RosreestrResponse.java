package com.ddkolesnik.trading.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

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

    private List<String> error;

}
