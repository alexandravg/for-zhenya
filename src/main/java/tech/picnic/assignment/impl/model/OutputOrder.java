package tech.picnic.assignment.impl.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Outcome order dto.
 */
public record OutputOrder(
        @JsonProperty("order_id") String orderId,
        @JsonProperty("amount") Long amount) {
}
