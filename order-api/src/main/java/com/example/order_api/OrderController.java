package com.example.order_api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for order management operations.
 */
@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Order management API")
@Validated
public class OrderController {

    private static final String MSG_ERROR_NOT_FOUND = "Order not found";
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    
    private final OrderService orderService;
    
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Create a new order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order created successfully",
                content = @Content(schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "400", description = "Invalid order data")
    })
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) {
        logger.info("Creating new order of type: {}", order.getType());
        Order newOrder = orderService.createOrder(order);
        logger.info("Created order with ID: {}", newOrder.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    @GetMapping
    @Operation(summary = "Retrieve a list of orders")
    public ResponseEntity<List<Order>> getOrders(
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(defaultValue = "10") int limit) {
        logger.info("Fetching orders with skip: {}, limit: {}", skip, limit);
        List<Order> orders = orderService.getAllOrders(skip, limit);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Retrieve a list of orders by status")
    public ResponseEntity<List<Order>> getOrdersByStatus(
            @PathVariable OrderStatus status,
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(defaultValue = "10") int limit) {
        logger.info("Fetching orders with status: {}, skip: {}, limit: {}", status, skip, limit);
        List<Order> orders = orderService.getOrdersByStatus(status, skip, limit);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a specific order by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the order"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<Object> getOrder(@PathVariable Long id) {
        logger.info("Fetching order with ID: {}", id);
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            return ResponseEntity.ok(order.get());
        } else {
            logger.warn("Order with ID: {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", MSG_ERROR_NOT_FOUND));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update the status of a specific order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid order status"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<Object> updateOrderStatus(
            @PathVariable Long id, 
            @RequestBody Map<String, String> payload) {
        
        String statusStr = payload.get("order_status");
        if (statusStr == null) {
            logger.warn("Missing status in request for order ID: {}", id);
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Missing 'order_status' in request body"));
        }
        
        OrderStatus status;
        try {
            status = OrderStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid status: {} for order ID: {}", statusStr, id);
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Invalid order status"));
        }
        
        logger.info("Updating order ID: {} to status: {}", id, status);
        Optional<Order> updatedOrder = orderService.updateOrderStatus(id, status);
        if (updatedOrder.isPresent()) {
            return ResponseEntity.ok(updatedOrder.get());
        } else {
            logger.warn("Order with ID: {} not found for status update", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", MSG_ERROR_NOT_FOUND));
        }
    }
}
