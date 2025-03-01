package com.sawmill.supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for managing stock operations in the supplier service.
 */
@Service
public class StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);
    private static final String TOPIC = "stock-updates";

    @Autowired
    private KafkaTemplate<String, Stock> kafkaTemplate;

    @Value("${supplier.api.url:http://stock-api:8080}")
    private String stockApiUrl;

    /**
     * Send stock update to Kafka
     * 
     * @param stock the stock update to send
     */
    public void sendStockUpdate(Stock stock) {
        logger.info("Sending stock update for {}: {}", stock.getWoodType(), stock.getQuantity());
        kafkaTemplate.send(TOPIC, stock.getWoodType().toString(), stock);
    }
}