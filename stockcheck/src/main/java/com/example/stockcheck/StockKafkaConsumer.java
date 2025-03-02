package com.example.stockcheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class StockKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(StockKafkaConsumer.class);
    
    private final StockApiClient stockApiClient;

    public StockKafkaConsumer(StockApiClient stockApiClient) {
        this.stockApiClient = stockApiClient;
    }

    @KafkaListener(topics = "${kafka.topic.stocks}", groupId = "${kafka.group-id}")
    public void listen(Stock stock) {
        logger.info("Received stock from Kafka: woodType={}, quantity={}", 
                stock.getWoodType(), stock.getQuantity());
        
        try {
            stockApiClient.createStock(stock);
        } catch (Exception e) {
            logger.error("Failed to process stock", e);
            // Consider implementing retry logic or dead-letter queue here
        }
    }
}
