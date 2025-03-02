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

        @GetMapping("/type/{woodType}")
        @Operation(summary = "Retrieve stock by wood type")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Found the stock item"),
                        @ApiResponse(responseCode = "404", description = "Stock item not found")
        })
        public ResponseEntity<Object> getStockByWoodType(@PathVariable WoodType woodType) {
                logger.info("GET request for stock with wood type: {}", woodType);
                return stockService.getStockByWoodType(woodType)
                                .map(stock -> ResponseEntity.ok().body((Object) stock))
                                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                                .body(Map.of("error", "Stock not found for wood type: " + woodType)));
        }

        @PutMapping("/type/{woodType}")
        @Operation(summary = "Update an existing stock item by wood type")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Stock updated successfully"),
                        @ApiResponse(responseCode = "404", description = "Stock not found")
        })
        public ResponseEntity<Object> updateStockByWoodType(@PathVariable WoodType woodType,
                        @Valid @RequestBody Stock stock) {
                logger.info("PUT request to update stock with wood type {}: {}", woodType, stock);
                return stockService.getStockByWoodType(woodType)
                                .map(existingStock -> {
                                        stock.setId(existingStock.getId());
                                        Stock savedStock = stockService.saveStock(stock);
                                        return ResponseEntity.ok().body((Object) savedStock);
                                })
                                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                                .body(Map.of("error", "Stock not found for wood type: " + woodType)));
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

        @DeleteMapping("/type/{woodType}")
        @Operation(summary = "Delete a stock item by wood type")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Stock deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "Stock not found")
        })
        public ResponseEntity<Object> deleteStockByWoodType(@PathVariable WoodType woodType) {
                logger.info("DELETE request for stock with wood type: {}", woodType);
                return stockService.getStockByWoodType(woodType)
                                .map(stock -> {
                                        stockService.deleteStock(stock.getId());
                                        return ResponseEntity.noContent().build();
                                })
                                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                                .body(Map.of("error", "Stock not found for wood type: " + woodType)));
        }

        @PutMapping("/add/{woodType}")
        @Operation(summary = "Add stock quantity, create if not exists")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Stock updated or created successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid quantity")
        })
        public ResponseEntity<Stock> addStock(@PathVariable WoodType woodType,
                        @RequestBody Map<String, Integer> payload) {
                Integer quantity = payload.get("quantity");
                if (quantity == null) {
                        logger.warn("Invalid quantity add request for wood type {}: {}", woodType, payload);
                        return ResponseEntity.badRequest().build();
                }

                logger.info("PUT request to add {} units to stock of wood type: {}", quantity, woodType);
                Stock updatedStock = stockService.addStock(woodType, quantity);
                return ResponseEntity.ok(updatedStock);
        }
}