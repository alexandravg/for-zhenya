package tech.picnic.assignment.impl.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Outcome delivery dto.
 */
public class OutputDelivery {

    @JsonProperty("delivery_id")
    private final String id;
    @JsonProperty("delivery_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private final Date deliveryTime;
    @JsonProperty("delivery_status")
    private OrderStatus status;
    @JsonProperty("orders")
    private List<OutputOrder> orders;
    @JsonProperty("total_amount")
    private Long totalAmount;

    public OutputDelivery(String id, Date deliveryTime, OrderStatus status, Long totalAmount, List<OutputOrder> orders) {
        this.id = id;
        this.deliveryTime = deliveryTime;
        this.status = status;
        this.totalAmount = totalAmount;
        this.orders = orders;
    }

    public void sumAmount(Long amount) {
        if (totalAmount != null) {
            if (amount != null)
                totalAmount += amount;
        } else {
            if (amount != null) {
                totalAmount = amount;
            }
        }
    }

    public void addOrder(OutputOrder order) {
        orders.add(order);
    }

    public void finalizeOrders() {
        orders = orders.stream()
                .sorted(Comparator.comparing(OutputOrder::orderId).reversed())
                .toList();
    }

    public String getId() {
        return this.id;
    }

    public Date getDeliveryTime() {
        return this.deliveryTime;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getTotalAmount() {
        return this.totalAmount;
    }

    public List<OutputOrder> getOrders() {
        return this.orders;
    }

    public String toString() {
        return "OutputDelivery(deliveryId=" + this.getId()
                + ", deliveryTime=" + this.getDeliveryTime()
                + ", deliveryStatus=" + this.getStatus()
                + ", totalAmount=" + this.getTotalAmount()
                + ", orders=" + this.getOrders()
                + ")";
    }
}
