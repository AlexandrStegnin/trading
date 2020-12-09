package com.ddkolesnik.trading.api;

import com.ddkolesnik.trading.model.dto.CadasterDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Data
public class RosreestrObject {

    @JsonProperty("lands")
    private List<CadasterDTO> lands;

    @JsonProperty("buildings")
    private List<CadasterDTO> buildings;

    @JsonProperty("rooms")
    private List<CadasterDTO> rooms;

    @JsonProperty("constructions")
    private List<CadasterDTO> constructions;

    @JsonProperty("quarters")
    private List<CadasterDTO> quarters;

    @JsonProperty("others")
    private List<CadasterDTO> others;

    public List<CadasterDTO> getAllObjects() {
        List<CadasterDTO> all = new ArrayList<>();
        if (lands != null) {
            all.addAll(lands);
        }
        if (buildings != null) {
            all.addAll(buildings);
        }
        if (rooms != null) {
            all.addAll(rooms);
        }
        if (constructions != null) {
            all.addAll(constructions);
        }
        if (quarters != null) {
            all.addAll(quarters);
        }
        if (others != null) {
            all.addAll(others);
        }
        return all;
    }

}
