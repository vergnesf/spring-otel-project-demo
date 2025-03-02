package com.example.supplier;

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

        // Récupérer tous les types de bois disponibles
        WoodType[] woodTypes = WoodType.values();
        // Sélectionner aléatoirement un type de bois
        WoodType randomWoodType = woodTypes[random.nextInt(woodTypes.length)];
        // Random quantity between 1 and 100
        int quantity = random.nextInt(100) + 1;
        Stock stock = new Stock(randomWoodType, quantity);
        stockService.sendStockUpdate(stock);
        logger.info("Stock sent: {}", stock);
    }
}