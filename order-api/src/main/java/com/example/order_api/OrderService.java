package com.example.order_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service handling business logic for order operations.
 */
@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    
    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Creates a new order in the system
     *
     * @param order the order to create
     * @return the created order with assigned ID
     */
    public Order createOrder(Order order) {
        // Set default status if not specified
        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.PENDING);
        }
        return orderRepository.save(order);
    }

    /**
     * Retrieves all orders with pagination
     *
     * @param skip number of records to skip
     * @param limit maximum number of records to return
     * @return list of orders
     */
    @Transactional(readOnly = true)
    public List<Order> getAllOrders(int skip, int limit) {
        int page = skip / limit;
        return orderRepository.findAll(
            PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"))
        ).getContent();
    }

    /**
     * Retrieves orders filtered by status with pagination
     *
     * @param status the status to filter by
     * @param skip number of records to skip
     * @param limit maximum number of records to return
     * @return list of orders with the specified status
     */
    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(OrderStatus status, int skip, int limit) {
        int page = skip / limit;
        return orderRepository.findByStatus(
            status, 
            PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
    }

    /**
     * Retrieves a specific order by ID
     *
     * @param id the order ID
     * @return an Optional containing the order if found
     */
    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * Updates the status of an existing order
     *
     * @param id the order ID
     * @param status the new status
     * @return an Optional containing the updated order if found
     */
    public Optional<Order> updateOrderStatus(Long id, OrderStatus status) {
        return orderRepository.findById(id)
            .map(order -> {
                order.setStatus(status);
                return orderRepository.save(order);
            });
    }
}
