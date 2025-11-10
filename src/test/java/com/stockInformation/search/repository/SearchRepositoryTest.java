package com.stockInformation.search.repository;

import com.stockInformation.cikLookup.entity.CikLookup;
import com.stockInformation.cikLookup.repository.CikLookupRepository;
import com.stockInformation.search.dto.AutocompleteResult;
import com.stockInformation.tickerSummary.entity.TickerSummary;
import com.stockInformation.tickerSummary.repository.TickerSummaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({SearchRepositoryImpl.class, com.stockInformation.config.QuerydslConfig.class})
public class SearchRepositoryTest {

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private CikLookupRepository cikLookupRepository;

    @Autowired
    private TickerSummaryRepository tickerSummaryRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // Create a simple similarity function for H2 testing
        jdbcTemplate.execute("CREATE ALIAS IF NOT EXISTS SIMILARITY DETERMINISTIC FOR \"com.stockInformation.search.repository.SearchRepositoryTest.simpleSimilarity\"");
    }

    public static double simpleSimilarity(String a, String b) {
        if (a == null || b == null) return 0.0;
        // Simple similarity: length of common prefix / max length
        int minLen = Math.min(a.length(), b.length());
        int common = 0;
        for (int i = 0; i < minLen; i++) {
            if (a.charAt(i) == b.charAt(i)) common++;
            else break;
        }
        return (double) common / Math.max(a.length(), b.length());
    }

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
