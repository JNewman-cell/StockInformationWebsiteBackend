package com.stockinfo.backend.repository;

import com.stockinfo.backend.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    
    Optional<Stock> findBySymbol(String symbol);
    
    List<Stock> findBySector(String sector);
    
    boolean existsBySymbol(String symbol);
}
