package tech.picnic.assignment.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.picnic.assignment.impl.model.InputOrder;
import tech.picnic.assignment.impl.model.OrderStatus;
import tech.picnic.assignment.impl.model.OutputDelivery;
import tech.picnic.assignment.impl.model.OutputOrder;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Service for reading and converting {@link InputOrder} to {@link OutputDelivery}.
 */
public class ConvertingService implements Runnable {

    private final ObjectMapper objectMapper;
    private final int maxOrders;
    private final InputStream source;
    private final List<InputOrder> orders;
    private final Map<String, OutputDelivery> deliveries;

    public ConvertingService(int maxOrders, InputStream source) {
        this.maxOrders = maxOrders;
        this.source = source;

        this.orders = new ArrayList<>();
        this.deliveries = new HashMap<>();
        this.objectMapper = ObjectMapperFactory.createObjectMapper();
    }

    /**
     * Runner for reading {@link InputOrder} records. Might stop when reach max orders or max duration.
     */
    @Override
    public void run() {
        int readOrders = 0;
        Scanner scanner = new Scanner(source, StandardCharsets.UTF_8);
        scanner.useDelimiter("\n");
        while (scanner.hasNext() && readOrders < maxOrders) {
            InputOrder order;
            try {
                order = objectMapper.readValue(scanner.next(), InputOrder.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Can not parse json", e);
            }
            orders.add(order);

            readOrders++;
        }
    }

    /**
     * Returns list of {@link OutputDelivery} converted from {@link InputOrder}.
     */
    public List<OutputDelivery> getDeliveries() {
        for (InputOrder inputOrder : orders) {
            convertInputToOutput(inputOrder);
        }
        return deliveries.values().stream().toList();
    }

    private void convertInputToOutput(InputOrder inputOrder) {
        if (inputOrder.orderStatus().equals(OrderStatus.CREATED)) return;

        OutputDelivery delivery = deliveries.get(inputOrder.delivery().deliveryId());
        if (delivery != null) {
            if (!delivery.getStatus().equals(OrderStatus.DELIVERED)
                    && inputOrder.orderStatus().equals(OrderStatus.DELIVERED)) {
                delivery.setStatus(inputOrder.orderStatus());
            }
            delivery.addOrder(new OutputOrder(inputOrder.orderId(), inputOrder.amount()));
            delivery.sumAmount(inputOrder.amount());
        } else {
            OutputOrder outputOrder = new OutputOrder(inputOrder.orderId(), inputOrder.amount());
            List<OutputOrder> newOrder = new ArrayList<>();
            newOrder.add(outputOrder);
            delivery = new OutputDelivery(
                    inputOrder.delivery().deliveryId(),
                    inputOrder.delivery().deliveryTime(),
                    inputOrder.orderStatus(),
                    outputOrder.amount(),
                    newOrder
            );
        }

        deliveries.put(delivery.getId(), delivery);
    }

}
