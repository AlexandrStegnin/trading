package com.ddkolesnik.trading.model;

import com.ddkolesnik.trading.model.dto.EgrnDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Alexandr Stegnin
 */

@Data
public class EgrnResponse {

    @JsonProperty("EGRN")
    private EgrnDTO egrnDTO;

}
