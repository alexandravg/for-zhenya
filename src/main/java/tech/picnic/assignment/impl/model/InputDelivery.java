package tech.picnic.assignment.impl.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Record of income delivery to be converted.
 */
public record InputDelivery(
        @JsonProperty("delivery_id") String deliveryId,
        @JsonProperty("delivery_time") Date deliveryTime) {
}
