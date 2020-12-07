package com.ddkolesnik.trading.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Alexandr Stegnin
 */

@Data
public class EgrnDetailsDTO {

    @JsonProperty("Этаж")
    private String floor;

    @JsonProperty("(ОКС) Тип")
    private String oksType;

}
