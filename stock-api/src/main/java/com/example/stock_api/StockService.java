package com.example.stock_api;

import com.example.stock_api.Stock;
import com.example.stock_api.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public Stock createStock(Stock stock) {
        return stockRepository.save(stock);
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Optional<Stock> getStockByWoodType(String woodType) {
        return stockRepository.findByWoodType(woodType);
    }

    public Stock decrementStock(String woodType, int quantity) {
        Optional<Stock> stockOptional = stockRepository.findByWoodType(woodType);
        if (stockOptional.isPresent()) {
            Stock stock = stockOptional.get();
            stock.setQuantity(stock.getQuantity() - quantity);
            return stockRepository.save(stock);
        }
        return null;
    }
}