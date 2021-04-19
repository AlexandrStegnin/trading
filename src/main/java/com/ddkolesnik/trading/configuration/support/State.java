package com.ddkolesnik.trading.configuration.support;

import lombok.Getter;

import java.util.Objects;

/**
 * @author Alexandr Stegnin
 */

@Getter
public enum State {

    ACTIVE(1, "Активная"),
    ARCHIVE(2, "Архивная");

    private final int id;

    private final String title;

    State(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public static State fromId(Integer id) {
        if (Objects.isNull(id)) {
            return ACTIVE;
        }
        for (State state : values()) {
            if (state.getId() == id) {
                return state;
            }
        }
        return ACTIVE;
    }

}
