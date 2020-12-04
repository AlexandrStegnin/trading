package com.ddkolesnik.trading.model;

import lombok.Data;

/**
 * @author Alexandr Stegnin
 */

@Data
public class RosreestrRequest {

    private String mode = "normal";

    private String query;

    private int grouped = 1;

}
