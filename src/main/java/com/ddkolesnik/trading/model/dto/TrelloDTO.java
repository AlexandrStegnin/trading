package com.ddkolesnik.trading.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author Alexandr Stegnin
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrelloDTO {

    @JsonProperty("name")
    String name;

    @JsonProperty("idMembers")
    String[] idMembers;

    @JsonProperty("desc")
    String description;

}
