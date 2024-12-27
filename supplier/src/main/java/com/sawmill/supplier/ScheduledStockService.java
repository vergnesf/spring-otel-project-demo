package com.sawmill.supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ScheduledStockService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledStockService.class);

    @Autowired
    private StockService stockService;

    private Random random = new Random();

    @Scheduled(fixedRateString = "${supplier.schedule.rate.ms:60000}") // Default to every 60 seconds
    public void sendScheduledStock() {
        int quantity = random.nextInt(100) + 1; // Random quantity between 1 and 100
        Stock stock = new Stock(WoodType.OAK, quantity); // You can change the wood type if necessary
        stockService.sendStock(stock);
        logger.info("Stock sent: {}", stock);
    }
}