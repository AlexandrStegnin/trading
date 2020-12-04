package com.ddkolesnik.trading.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Data
public class RosreestrObject {

    @JsonProperty("lands")
    private List<CadasterEntity> lands;

    @JsonProperty("buildings")
    private List<CadasterEntity> buildings;

    @JsonProperty("rooms")
    private List<CadasterEntity> rooms;

    @JsonProperty("constructions")
    private List<CadasterEntity> constructions;

    @JsonProperty("quarters")
    private List<CadasterEntity> quarters;

    @JsonProperty("others")
    private List<CadasterEntity> others;

}
