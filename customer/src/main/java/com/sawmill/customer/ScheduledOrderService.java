package com.sawmill.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ScheduledOrderService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledOrderService.class);

    @Autowired
    private OrderService orderService;

    private Random random = new Random();

    @Scheduled(fixedRateString = "${order.schedule.rate.ms:60000}") // Default to every 60 seconds
    public void sendScheduledOrder() {
        int quantity = random.nextInt(100) + 1; // Random quantity between 1 and 100
        Order order = new Order(WoodType.OAK, quantity); // You can change the wood type if necessary
        orderService.sendOrder(order);
        logger.info("Order sent: {}", order);
    }
}