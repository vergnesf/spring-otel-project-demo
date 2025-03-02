package com.example.order_api;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Order entity operations.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Find orders by their status with pagination support
     * 
     * @param status the status to filter by
     * @param pageable pagination information
     * @return a list of matching orders
     */
    List<Order> findByStatus(OrderStatus status, Pageable pageable);
    
    /**
     * Find orders by their status
     * 
     * @param status the status to filter by
     * @return a list of matching orders
     */
    List<Order> findByStatus(OrderStatus status);
}
