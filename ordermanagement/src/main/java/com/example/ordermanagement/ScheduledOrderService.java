package com.example.ordermanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@Service
public class ScheduledOrderService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledOrderService.class);
    
    private final RestTemplate restTemplate;
    private final String orderApiUrl;
    private final String stockApiUrl;
    
    @Autowired
    public ScheduledOrderService(RestTemplate restTemplate, 
            @Value("${api.order.url}") String orderApiUrl,
            @Value("${api.stock.url}") String stockApiUrl) {
        this.restTemplate = restTemplate;
        this.orderApiUrl = orderApiUrl;
        this.stockApiUrl = stockApiUrl;
    }

    @Scheduled(fixedRateString = "${ordermanagement.schedule.rate.ms:60000}")
    public void sendScheduledOrder() {
        logger.info("Retrieving pending orders");

        try {
            // Building the URL to retrieve 20 orders with PENDING status
            String url = orderApiUrl + "/api/orders/status/PENDING?skip=0&limit=5";
            logger.info("Calling API: {}", url);
            
            // Using ParameterizedTypeReference to handle the list of objects
            ResponseEntity<List<Order>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Order>>() {}
            );
            
            List<Order> pendingOrders = response.getBody();
            
            if (pendingOrders != null) {
                logger.info("Number of retrieved pending orders: {}", pendingOrders.size());
                
                // Processing pending orders
                for (Order order : pendingOrders) {
                    processOrder(order);
                }
            } else {
                logger.info("No pending orders found");
            }
        } catch (Exception e) {
            logger.error("Error retrieving pending orders: {}", e.getMessage(), e);
        }
    }
    
    private void processOrder(Order order) {
        logger.info("Processing order: ID={}, Type={}, Quantity={}", 
                order.getId(), order.getWoodType(), order.getQuantity());
        
        try {
            // 1. Check available stock
            String stockUrl = stockApiUrl + "/api/stock/type/" + order.getWoodType();
            logger.info("Checking stock: {}", stockUrl);
            
            ResponseEntity<Stock> stockResponse = restTemplate.getForEntity(stockUrl, Stock.class);
            Stock currentStock = stockResponse.getBody();
            
            if (currentStock == null) {
                logger.warn("No stock found for wood type: {}", order.getWoodType());
                updateOrderStatus(order.getId(), "BLOCKED");
                return;
            }
            
            logger.info("Available stock: {} units for {}", currentStock.getQuantity(), order.getWoodType());
            
            // 2. Check if stock is sufficient
            if (currentStock.getQuantity() >= order.getQuantity()) {
                // 3. Update stock (decrement)
                int newQuantity = currentStock.getQuantity() - order.getQuantity();
                updateStock(order.getWoodType(), newQuantity);
                
                // 4. Update order status to PROCESSING
                updateOrderStatus(order.getId(), "PROCESSING");
                
                logger.info("Order {} successfully updated. New stock for {}: {}", 
                        order.getId(), order.getWoodType(), newQuantity);
            } else {
                // Insufficient stock, mark order as blocked
                logger.warn("Insufficient stock for order {}. Available: {}, Required: {}", 
                        order.getId(), currentStock.getQuantity(), order.getQuantity());
                updateOrderStatus(order.getId(), "BLOCKED");
            }
        } catch (Exception e) {
            logger.error("Error processing order {}: {}", order.getId(), e.getMessage(), e);
        }
    }
    
    /**
     * Updates stock for a given wood type
     */
    private void updateStock(WoodType woodType, int newQuantity) {
        String url = stockApiUrl + "/api/stock/type/" + woodType;
        logger.info("Updating stock: {} units for {}", newQuantity, woodType);
        
        // Create stock object with new quantity
        Stock updatedStock = new Stock();
        updatedStock.setWoodType(woodType);
        updatedStock.setQuantity(newQuantity);
        
        restTemplate.put(url, updatedStock);
    }
    
    /**
     * Updates an order status
     */
    private void updateOrderStatus(Long orderId, String newStatus) {
        String url = orderApiUrl + "/api/orders/" + orderId;
        logger.info("Updating order status {}: {}", orderId, newStatus);
        
        // Create a simple map with the required field name 'order_status'
        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("order_status", newStatus);
        
        // Send the status update request
        try {
            restTemplate.put(url, statusUpdate);
            logger.info("Order status {} updated to: {}", orderId, newStatus);
        } catch (Exception e) {
            logger.error("Failed to update order status: {}", e.getMessage());
        }
    }
    }