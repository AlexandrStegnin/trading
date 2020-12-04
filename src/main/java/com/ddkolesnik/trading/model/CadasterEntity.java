package com.ddkolesnik.trading.model;

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

    private String address;

    private String cadNumber;

    private String type;

    private String area;

    private String category;

}
