package com.stockInformation.search.repository;

import com.stockInformation.cikLookup.entity.CikLookup;
import com.stockInformation.cikLookup.repository.CikLookupRepository;
import com.stockInformation.search.dto.AutocompleteResult;
import com.stockInformation.tickerSummary.entity.TickerSummary;
import com.stockInformation.tickerSummary.repository.TickerSummaryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(SearchRepositoryImpl.class)
class SearchRepositoryTest {

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private CikLookupRepository cikLookupRepository;

    @Autowired
    private TickerSummaryRepository tickerSummaryRepository;

    @Test
    void testSearchByInputIgnoreCase() {
        // Given
        CikLookup cikLookup1 = new CikLookup(320193, "Apple Inc.");
        CikLookup cikLookup2 = new CikLookup(789019, "Microsoft Corporation");
        cikLookupRepository.save(cikLookup1);
        cikLookupRepository.save(cikLookup2);

        TickerSummary ticker1 = new TickerSummary("AAPL", new BigDecimal("150.00"));
        ticker1.setCikLookup(cikLookup1);
        ticker1.setMarketCap(2000000000000L);
        ticker1.setFiftyDayAverage(BigDecimal.ZERO);
        ticker1.setTwoHundredDayAverage(BigDecimal.ZERO);

        TickerSummary ticker2 = new TickerSummary("MSFT", new BigDecimal("300.00"));
        ticker2.setCikLookup(cikLookup2);
        ticker2.setMarketCap(2000000000000L);
        ticker2.setFiftyDayAverage(BigDecimal.ZERO);
        ticker2.setTwoHundredDayAverage(BigDecimal.ZERO);

        tickerSummaryRepository.save(ticker1);
        tickerSummaryRepository.save(ticker2);

        // When
        List<AutocompleteResult> results = searchRepository.searchByInputIgnoreCase("apple");

        // Then
        assertThat(results).hasSize(1);
        AutocompleteResult result = results.get(0);
        assertThat(result.symbol()).isEqualTo("AAPL");
        assertThat(result.name()).isEqualTo("Apple Inc.");
        assertThat(result.score()).isGreaterThan(0.4);
    }

    @Test
    void testSearchByInputIgnoreCaseNoMatch() {
        // Given - no data

        // When
        List<AutocompleteResult> results = searchRepository.searchByInputIgnoreCase("nonexistent");

        // Then
        assertThat(results).isEmpty();
    }
}
