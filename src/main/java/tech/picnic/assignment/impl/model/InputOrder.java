package tech.picnic.assignment.impl.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Record of income order to be converted.
 */
public record InputOrder(
        @JsonProperty("order_id") String orderId,
        @JsonProperty("order_status") OrderStatus orderStatus,
        @JsonProperty("delivery") InputDelivery delivery,
        @JsonProperty("amount") Long amount) {
}
