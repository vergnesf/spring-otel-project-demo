package com.example.stock_api;

import com.example.stock_api.Stock;
import com.example.stock_api.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping
    public Stock createStock(@RequestBody Stock stock) {
        return stockService.createStock(stock);
    }

    @GetMapping
    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    @GetMapping("/{woodType}")
    public ResponseEntity<Stock> getStockByWoodType(@PathVariable String woodType) {
        Optional<Stock> stock = stockService.getStockByWoodType(woodType);
        return stock.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/decrement/{woodType}")
    public ResponseEntity<Stock> decrementStock(@PathVariable String woodType, @RequestParam int quantity) {
        Stock stock = stockService.decrementStock(woodType, quantity);
        return stock != null ? ResponseEntity.ok(stock) : ResponseEntity.notFound().build();
    }
}