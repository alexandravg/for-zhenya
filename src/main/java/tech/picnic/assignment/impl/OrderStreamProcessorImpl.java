package tech.picnic.assignment.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import tech.picnic.assignment.api.OrderStreamProcessor;
import tech.picnic.assignment.impl.model.OutputDelivery;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

/**
 * Implementation of {@link OrderStreamProcessor}.
 */
public class OrderStreamProcessorImpl implements OrderStreamProcessor {

    private final ObjectMapper objectMapper;
    private final int maxOrders;
    private final Duration maxTime;

    public OrderStreamProcessorImpl(int maxOrders, Duration maxTime) {
        this.maxOrders = maxOrders;
        this.maxTime = maxTime;

        this.objectMapper = ObjectMapperFactory.createObjectMapper();

    }

    @Override
    public void process(InputStream source, OutputStream sink) throws IOException {
        ConvertingService service = new ConvertingService(maxOrders, source);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future future = executor.submit(service);
        try {
            future.get(maxTime.getSeconds(), TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            List<OutputDelivery> deliveryList = service.getDeliveries()
                    .stream()
                    .sorted(Comparator.comparing(OutputDelivery::getDeliveryTime))
                    .toList();
            for (OutputDelivery outputDelivery : deliveryList) {
                outputDelivery.finalizeOrders();
            }
            sink.write(objectMapper.writeValueAsBytes(deliveryList));
            executor.shutdownNow();
        }
    }

}
