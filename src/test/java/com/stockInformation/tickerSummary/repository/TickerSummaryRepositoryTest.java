package com.stockInformation.tickerSummary.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.stockInformation.tickerSummary.entity.TickerSummary;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TickerSummaryRepositoryTest {

    @Autowired
    private TickerSummaryRepository tickerSummaryRepository;

    @Test
    void testSaveAndFindByTickerIgnoreCase() {
        // Given
        TickerSummary tickerSummary = new TickerSummary("AAPL", new BigDecimal("150.00"));
        tickerSummary.setMarketCap(2000000000000L);
        tickerSummary.setFiftyDayAverage(BigDecimal.ZERO);
        tickerSummary.setTwoHundredDayAverage(BigDecimal.ZERO);

        // When
        tickerSummaryRepository.save(tickerSummary);
        Optional<TickerSummary> found = tickerSummaryRepository.findByTickerIgnoreCase("aapl");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getTicker()).isEqualTo("AAPL");
        assertThat(found.get().getPreviousClose()).isEqualByComparingTo(new BigDecimal("150.00"));
    }
}