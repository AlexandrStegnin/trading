package com.ddkolesnik.trading.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Alexandr Stegnin
 */

@Data
@Entity
@Table(name = "cadaster")
@EqualsAndHashCode(callSuper = true)
public class CadasterEntity extends BaseEntity {

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
