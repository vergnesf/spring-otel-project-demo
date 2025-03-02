package com.example.stock_api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Stock entity operations.
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    /**
     * Find stock by wood type
     *
     * @param woodType the type of wood to search for
     * @return an Optional containing the stock if found
     */
    Optional<Stock> findByWoodType(WoodType woodType);
}