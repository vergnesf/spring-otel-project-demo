package com.example.ordermanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ScheduledOrderService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledOrderService.class);


    @Scheduled(fixedRateString = "${ordermanagement.schedule.rate.ms:60000}")
    public void sendScheduledOrder() {
        logger.info("Sending scheduled order");

        

    }
}