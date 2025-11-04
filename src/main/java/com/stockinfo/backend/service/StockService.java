package com.stockinfo.backend.service;

import com.stockinfo.backend.entity.Stock;
import com.stockinfo.backend.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StockService {

    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Optional<Stock> getStockById(Long id) {
        return stockRepository.findById(id);
    }

    public Optional<Stock> getStockBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol);
    }

    public List<Stock> getStocksBySector(String sector) {
        return stockRepository.findBySector(sector);
    }

    public Stock createStock(Stock stock) {
        if (stockRepository.existsBySymbol(stock.getSymbol())) {
            throw new IllegalArgumentException("Stock with symbol " + stock.getSymbol() + " already exists");
        }
        return stockRepository.save(stock);
    }

    public Stock updateStock(Long id, Stock stockDetails) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with id: " + id));

        // Note: Symbol is not updated to maintain unique constraint integrity
        stock.setCompanyName(stockDetails.getCompanyName());
        stock.setCurrentPrice(stockDetails.getCurrentPrice());
        stock.setPreviousClose(stockDetails.getPreviousClose());
        stock.setDayHigh(stockDetails.getDayHigh());
        stock.setDayLow(stockDetails.getDayLow());
        stock.setVolume(stockDetails.getVolume());
        stock.setSector(stockDetails.getSector());

        return stockRepository.save(stock);
    }

    public void deleteStock(Long id) {
        if (!stockRepository.existsById(id)) {
            throw new IllegalArgumentException("Stock not found with id: " + id);
        }
        stockRepository.deleteById(id);
    }
}
