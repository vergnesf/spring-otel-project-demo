package com.example.ordercheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class OrderKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderKafkaConsumer.class);
    
    private final OrderApiClient orderApiClient;

    public OrderKafkaConsumer(OrderApiClient orderApiClient) {
        this.orderApiClient = orderApiClient;
    }

    @KafkaListener(topics = "${kafka.topic.orders}", groupId = "${kafka.group-id}")
    public void listen(Order order) {
        logger.info("Received order from Kafka: woodType={}, quantity={}", 
                 order.getWoodType(), order.getQuantity());
        
        try {
            orderApiClient.createOrder(order);
        } catch (Exception e) {
            logger.error("Failed to process order", e);
            // Consider implementing retry logic or dead-letter queue here
        }
    }
}
