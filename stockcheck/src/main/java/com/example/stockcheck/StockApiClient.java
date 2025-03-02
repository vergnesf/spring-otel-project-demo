package com.example.stockcheck;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    /**
     * Ajoute une quantité au stock existant, ou crée un nouveau stock si nécessaire
     * 
     * @param woodType Le type de bois à ajouter
     * @param quantity La quantité à ajouter
     * @return Le stock mis à jour
     */
    public Stock addStock(String woodType, int quantity) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Création du payload avec la quantité à ajouter
            Map<String, Integer> payload = new HashMap<>();
            payload.put("quantity", quantity);

            HttpEntity<Map<String, Integer>> request = new HttpEntity<>(payload, headers);

            String url = stockApiUrl + "/add/" + woodType;
            logger.info("Ajout de {} unités au stock de type {}: {}", quantity, woodType, url);

            // Appel de l'API avec PUT
            ResponseEntity<Stock> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    request,
                    Stock.class);

            Stock updatedStock = response.getBody();
            logger.info("Stock mis à jour avec succès: type={}, quantité={}",
                    woodType, updatedStock != null ? updatedStock.getQuantity() : "unknown");

            return updatedStock;
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout au stock pour le type de bois {}: {}", woodType, e.getMessage(), e);
            throw e;
        }
    }
}
