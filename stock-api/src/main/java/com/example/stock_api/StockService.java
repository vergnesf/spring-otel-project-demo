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
    public Optional<Stock> getStockByWoodType(String woodType) {
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
}