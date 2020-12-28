package com.ddkolesnik.trading.model.dto;

import lombok.Data;

/**
 * @author Alexandr Stegnin
 */

@Data
public class FiasSearchContextDTO {

    private String oneString;

    private String query;

    private int limit;

}
