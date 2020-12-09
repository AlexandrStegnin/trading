package com.ddkolesnik.trading.api;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Alexandr Stegnin
 */

@Data
@NoArgsConstructor
public class RosreestrRequest {

    private String mode;

    private String query;

    private int grouped;

    public RosreestrRequest(String mode, String query, int grouped) {
        this.mode = mode;
        this.grouped = grouped;
        this.query = query;
    }

    public RosreestrRequest(String query) {
        this.query = query;
    }

}
