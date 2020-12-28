package com.ddkolesnik.trading.api;

import com.ddkolesnik.trading.model.dto.FiasResponseDTO;
import com.ddkolesnik.trading.model.dto.FiasSearchContextDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Data
public class FiasResponse {

    @JsonProperty("searchContext")
    private FiasSearchContextDTO searchContext;

    @JsonProperty("result")
    private List<FiasResponseDTO> result;

}
