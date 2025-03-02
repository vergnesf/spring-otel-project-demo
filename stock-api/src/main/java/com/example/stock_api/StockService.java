package com.example.stock_api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service handling business logic for stock operations.
 */
@Service
@Transactional
public class StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);

    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    /**
     * Retrieve all stock items
     * 
     * @return list of all stock items
     */
    @Transactional(readOnly = true)
    public List<Stock> getAllStock() {
        logger.info("Retrieving all stock items");
        return stockRepository.findAll();
    }

    /**
     * Find stock by wood type
     * 
     * @param woodType the type of wood to search for
     * @return an Optional containing the stock if found
     */
    @Transactional(readOnly = true)
    public Optional<Stock> getStockByWoodType(WoodType woodType) {
        logger.info("Looking up stock for wood type: {}", woodType);
        return stockRepository.findByWoodType(woodType);
    }

    /**
     * Create or update a stock item
     * 
     * @param stock the stock to save
     * @return the saved stock
     */
    public Stock saveStock(Stock stock) {
        logger.info("Saving stock: {}", stock);
        return stockRepository.save(stock);
    }

    /**
     * Update stock quantity
     * 
     * @param id       the stock ID
     * @param quantity the new quantity
     * @return an Optional containing the updated stock if found
     */
    public Optional<Stock> updateQuantity(Long id, Integer quantity) {
        logger.info("Updating quantity for stock ID {}: {}", id, quantity);
        return stockRepository.findById(id)
                .map(stock -> {
                    stock.setQuantity(quantity);
                    return stockRepository.save(stock);
                });
    }

    /**
     * Delete stock by ID
     * 
     * @param id the stock ID to delete
     */
    public void deleteStock(Long id) {
        logger.info("Deleting stock ID: {}", id);
        stockRepository.deleteById(id);
    }

    /**
     * Adds quantity to existing stock or creates a new stock if it doesn't exist
     * 
     * @param woodType the wood type
     * @param quantity the quantity to add
     * @return the updated or created stock
     */
    public Stock addStock(WoodType woodType, int quantity) {
        try {
            // Convertir la chaîne en enum WoodType
            Optional<Stock> stockOptional = stockRepository.findByWoodType(woodType);

            if (!stockOptional.isPresent()) {
                logger.info("Stock not found for wood type {}, creating new stock", woodType);
                Stock newStock = new Stock();
                newStock.setWoodType(woodType);
                newStock.setQuantity(quantity);
                return saveStock(newStock);
            }

            Stock stock = stockOptional.get();
            stock.setQuantity(stock.getQuantity() + quantity);
            return saveStock(stock);
        } catch (IllegalArgumentException e) {
            logger.error("Erreur lors de la conversion du type de bois {} en énumération WoodType", woodType, e);
            throw new IllegalArgumentException(
                    "Type de bois non valide : " + woodType + ". Les valeurs valides sont : " +
                            java.util.Arrays.toString(WoodType.values()));
        }
    }
}