package com.stockinfo.backend.repository;

import com.stockinfo.backend.entity.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Test
    void testSaveAndFindStock() {
        // Given
        Stock stock = new Stock("AAPL", "Apple Inc.", new BigDecimal("150.00"));
        
        // When
        Stock savedStock = stockRepository.save(stock);
        Optional<Stock> foundStock = stockRepository.findById(savedStock.getId());
        
        // Then
        assertThat(foundStock).isPresent();
        assertThat(foundStock.get().getSymbol()).isEqualTo("AAPL");
        assertThat(foundStock.get().getCompanyName()).isEqualTo("Apple Inc.");
    }

    @Test
    void testFindBySymbol() {
        // Given
        Stock stock = new Stock("GOOGL", "Alphabet Inc.", new BigDecimal("2800.00"));
        stockRepository.save(stock);
        
        // When
        Optional<Stock> foundStock = stockRepository.findBySymbol("GOOGL");
        
        // Then
        assertThat(foundStock).isPresent();
        assertThat(foundStock.get().getCompanyName()).isEqualTo("Alphabet Inc.");
    }

    @Test
    void testExistsBySymbol() {
        // Given
        Stock stock = new Stock("MSFT", "Microsoft Corporation", new BigDecimal("300.00"));
        stockRepository.save(stock);
        
        // When
        boolean exists = stockRepository.existsBySymbol("MSFT");
        boolean notExists = stockRepository.existsBySymbol("UNKNOWN");
        
        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
