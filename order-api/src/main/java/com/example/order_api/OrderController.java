package com.example.order_api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final String MSG_ERROR_NOT_FOUND = "Order not found";
    
    @Autowired
    private OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order created successfully",
                content = @Content(schema = @Schema(implementation = Order.class)))
    })
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order newOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    @GetMapping
    @Operation(summary = "Retrieve a list of orders")
    public List<Order> getOrders(
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(defaultValue = "10") int limit) {
        return orderService.getAllOrders(skip, limit);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Retrieve a list of orders by status")
    public List<Order> getOrdersByStatus(
            @PathVariable OrderStatus status,
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(defaultValue = "10") int limit) {
        return orderService.getOrdersByStatus(status, skip, limit);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a specific order by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the order"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<Object> getOrder(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            return ResponseEntity.ok(order.get());
        } else {
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
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Missing 'order_status' in request body"));
        }
        
        OrderStatus status;
        try {
            status = OrderStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Invalid order status"));
        }
        
        Optional<Order> updatedOrder = orderService.updateOrderStatus(id, status);
        if (updatedOrder.isPresent()) {
            return ResponseEntity.ok(updatedOrder.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", MSG_ERROR_NOT_FOUND));
        }
    }
}
