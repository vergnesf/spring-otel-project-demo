package com.example.stockcheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class StockApiClient {

    private static final Logger logger = LoggerFactory.getLogger(StockApiClient.class);

    private final RestTemplate restTemplate;
    private final String stockApiUrl;

    public StockApiClient(RestTemplate restTemplate, @Value("${api.stock.url}") String stockApiUrl) {
        this.restTemplate = restTemplate;
        this.stockApiUrl = stockApiUrl + "/api/stock";
    }

    public void createStock(Stock stock) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Stock> request = new HttpEntity<>(stock, headers);
            
            logger.info("Sending stock to API: {}", stock);
            
            restTemplate.postForEntity(stockApiUrl, request, Object.class);
            
            logger.info("Stock successfully created for wood type: {}, quantity: {}", 
                    stock.getWoodType(), stock.getQuantity());
            
        } catch (Exception e) {
            logger.error("Error creating stock via API for wood type: {}", stock.getWoodType(), e);
            throw e;
        }
    }
}
