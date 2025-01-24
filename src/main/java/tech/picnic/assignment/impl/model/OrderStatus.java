package tech.picnic.assignment.impl.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration of possible order or delivery statuses.
 */
public enum OrderStatus {
    DELIVERED("delivered"),
    CREATED("created"),
    CANCELLED("cancelled");

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
