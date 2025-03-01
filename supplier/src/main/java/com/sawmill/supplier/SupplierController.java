package com.sawmill.supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for supplier operations.
 */
@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    private static final Logger logger = LoggerFactory.getLogger(SupplierController.class);

    @Autowired
    private StockService stockService;

    @PostMapping("/stock")
    public ResponseEntity<String> addStock(@RequestBody Stock stock) {
        logger.info("Received stock add request: {}", stock);
        stockService.sendStockUpdate(stock);
        return ResponseEntity.ok("Stock update sent successfully");
    }
}
