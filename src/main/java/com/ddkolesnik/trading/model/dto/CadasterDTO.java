package com.ddkolesnik.trading.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Alexandr Stegnin
 */

@Data
public class CadasterDTO {

    @JsonProperty("ADDRESS")
    private String address;

    @JsonProperty("CADNOMER")
    private String cadNumber;

    @JsonProperty("TYPE")
    private String type;

    @JsonProperty("AREA")
    private String area;

    @JsonProperty("CATEGORY")
    private String category;

}
