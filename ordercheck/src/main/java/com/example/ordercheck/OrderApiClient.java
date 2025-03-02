package com.example.ordercheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class OrderApiClient {

    private static final Logger logger = LoggerFactory.getLogger(OrderApiClient.class);

    private final RestTemplate restTemplate;
    private final String orderApiUrl;

    public OrderApiClient(RestTemplate restTemplate, @Value("${api.order.url}") String orderApiUrl) {
        this.restTemplate = restTemplate;
        this.orderApiUrl = orderApiUrl + "/api/orders";
    }

    public void createOrder(Order order) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Order> request = new HttpEntity<>(order, headers);
            
            logger.info("Sending order to API: {}", order);
            
            restTemplate.postForEntity(orderApiUrl, request, Object.class);
            
            logger.info("Order successfully created for wood type: {}, quantity: {}", 
                    order.getWoodType(), order.getQuantity());
            
        } catch (Exception e) {
            logger.error("Error creating order via API for wood type: {}", order.getWoodType(), e);
            throw e;
        }
    }
}
