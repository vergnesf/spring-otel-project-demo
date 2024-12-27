package com.sawmill.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private static final String TOPIC = "stocks";

    @Autowired
    private KafkaTemplate<String, Stock> kafkaTemplate;

    public void sendStock(Stock stock) {
        kafkaTemplate.send(TOPIC, stock);
    }
}