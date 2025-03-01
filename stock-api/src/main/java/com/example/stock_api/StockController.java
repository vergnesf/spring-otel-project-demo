package com.example.stock_api;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for stock management operations.
 */
@RestController
@RequestMapping("/api/stock")
@Tag(name = "Stock", description = "Stock management API")
public class StockController {

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all stock items")
    public ResponseEntity<List<Stock>> getAllStock() {
        logger.info("GET request for all stock items");
        return ResponseEntity.ok(stockService.getAllStock());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a specific stock item by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the stock item"),
            @ApiResponse(responseCode = "404", description = "Stock item not found")
    })
    public ResponseEntity<Object> getStockById(@PathVariable Long id) {
        logger.info("GET request for stock ID: {}", id);
        return stockService.getStockByWoodType(id)
                .map(stock -> ResponseEntity.ok().body((Object) stock))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Stock not found")));
    }

    @GetMapping("/type/{woodType}")
    @Operation(summary = "Retrieve stock by wood type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the stock item"),
            @ApiResponse(responseCode = "404", description = "Stock item not found")
    })
    public ResponseEntity<Object> getStockByWoodType(@PathVariable String woodType) {
        logger.info("GET request for stock with wood type: {}", woodType);
        return stockService.getStockByWoodType(woodType)
                .map(stock -> ResponseEntity.ok().body((Object) stock))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Stock not found for wood type: " + woodType)));
    }

    @PostMapping
    @Operation(summary = "Create a new stock item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Stock created successfully", content = @Content(schema = @Schema(implementation = Stock.class))),
            @ApiResponse(responseCode = "400", description = "Invalid stock data")
    })
    public ResponseEntity<Stock> createStock(@Valid @RequestBody Stock stock) {
        logger.info("POST request to create stock: {}", stock);
        Stock savedStock = stockService.saveStock(stock);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStock);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing stock item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock updated successfully"),
            @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    public ResponseEntity<Object> updateStock(@PathVariable Long id, @Valid @RequestBody Stock stock) {
        logger.info("PUT request to update stock ID {}: {}", id, stock);
        return stockService.getStockByWoodType(id)
                .map(existingStock -> {
                    stock.setId(id);
                    return ResponseEntity.ok().body(stockService.saveStock(stock));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Stock not found")));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update stock quantity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock quantity updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid quantity"),
            @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    public ResponseEntity<Object> updateQuantity(@PathVariable Long id, @RequestBody Map<String, Integer> payload) {
        Integer quantity = payload.get("quantity");
        if (quantity == null) {
            logger.warn("Invalid quantity update request for stock ID {}: {}", id, payload);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Missing 'quantity' in request body"));
        }

        logger.info("PATCH request to update quantity for stock ID {}: {}", id, quantity);
        return stockService.updateQuantity(id, quantity)
                .map(stock -> ResponseEntity.ok().body((Object) stock))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Stock not found")));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a stock item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Stock deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    public ResponseEntity<Object> deleteStock(@PathVariable Long id) {
        logger.info("DELETE request for stock ID: {}", id);
        if (stockService.getStockByWoodType(id).isPresent()) {
            stockService.deleteStock(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Stock not found"));
        }
    }
}