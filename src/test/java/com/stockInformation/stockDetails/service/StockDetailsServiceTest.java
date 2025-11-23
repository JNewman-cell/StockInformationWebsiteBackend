package com.stockInformation.stockDetails.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.stockInformation.stockDetails.dto.DetailsSummaryResponse;
import com.stockInformation.stockDetails.entity.TickerOverview;
import com.stockInformation.stockDetails.repository.TickerOverviewRepository;
import com.stockInformation.tickerSummary.entity.TickerSummary;
import com.stockInformation.tickerSummary.repository.TickerSummaryRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockDetailsServiceTest {

    @Mock
    private TickerSummaryRepository tickerSummaryRepository;

    @Mock
    private TickerOverviewRepository tickerOverviewRepository;

    @InjectMocks
    private StockDetailsService stockDetailsService;

    @Test
    void testGetStockDetailsSummaryByTicker_Found_ReturnsResponse() {
        // Given
        String ticker = "AAPL";
        TickerSummary summary = new TickerSummary(ticker, BigDecimal.valueOf(150.00));
        summary.setFiftyDayAverage(BigDecimal.valueOf(145.00));
        summary.setTwoHundredDayAverage(BigDecimal.valueOf(140.00));
        TickerOverview overview = new TickerOverview();
        overview.setTicker(ticker);
        overview.setEbitdaMargin(BigDecimal.valueOf(28.50));
        overview.setEarningsGrowth(BigDecimal.valueOf(15.70));
        overview.setRevenueGrowth(BigDecimal.valueOf(10.20));
        overview.setTrailingEps(BigDecimal.valueOf(6.15));
        overview.setForwardEps(BigDecimal.valueOf(7.25));
        overview.setPegRatio(BigDecimal.valueOf(1.80));

        when(tickerSummaryRepository.findByTickerIgnoreCase("aapl")).thenReturn(Optional.of(summary));
        when(tickerOverviewRepository.findByTickerIgnoreCase("aapl")).thenReturn(Optional.of(overview));

        // When
        Optional<DetailsSummaryResponse> result = stockDetailsService.getStockDetailsSummaryByTicker(ticker);

        // Then
        assertThat(result).isPresent();
        verify(tickerSummaryRepository).findByTickerIgnoreCase("aapl");
        verify(tickerOverviewRepository).findByTickerIgnoreCase("aapl");
    }

    @Test
    void testGetStockDetailsSummaryByTicker_SummaryNotFound_ReturnsEmpty() {
        // Given
        String ticker = "AAPL";
        when(tickerSummaryRepository.findByTickerIgnoreCase("aapl")).thenReturn(Optional.empty());
        when(tickerOverviewRepository.findByTickerIgnoreCase("aapl")).thenReturn(Optional.of(new TickerOverview()));

        // When
        Optional<DetailsSummaryResponse> result = stockDetailsService.getStockDetailsSummaryByTicker(ticker);

        // Then
        assertThat(result).isEmpty();
        verify(tickerSummaryRepository).findByTickerIgnoreCase("aapl");
        verify(tickerOverviewRepository).findByTickerIgnoreCase("aapl");
    }

    @Test
    void testGetStockDetailsSummaryByTicker_OverviewNotFound_ReturnsEmpty() {
        // Given
        String ticker = "AAPL";
        TickerSummary summary = new TickerSummary(ticker, BigDecimal.valueOf(150.00));

        when(tickerSummaryRepository.findByTickerIgnoreCase("aapl")).thenReturn(Optional.of(summary));
        when(tickerOverviewRepository.findByTickerIgnoreCase("aapl")).thenReturn(Optional.empty());

        // When
        Optional<DetailsSummaryResponse> result = stockDetailsService.getStockDetailsSummaryByTicker(ticker);

        // Then
        assertThat(result).isEmpty();
        verify(tickerSummaryRepository).findByTickerIgnoreCase("aapl");
        verify(tickerOverviewRepository).findByTickerIgnoreCase("aapl");
    }

    @Test
    void testGetStockDetailsSummaryByTicker_LowercaseTicker() {
        // Given
        String ticker = "aapl";
        TickerSummary summary = new TickerSummary("AAPL", BigDecimal.valueOf(150.00));
        summary.setFiftyDayAverage(BigDecimal.valueOf(145.00));
        summary.setTwoHundredDayAverage(BigDecimal.valueOf(140.00));
        TickerOverview overview = new TickerOverview();
        overview.setTicker("AAPL");
        overview.setEbitdaMargin(BigDecimal.valueOf(28.50));
        overview.setEbitdaMargin(BigDecimal.valueOf(28.50));
        overview.setEarningsGrowth(BigDecimal.valueOf(15.70));
        overview.setRevenueGrowth(BigDecimal.valueOf(10.20));
        overview.setTrailingEps(BigDecimal.valueOf(6.15));
        overview.setForwardEps(BigDecimal.valueOf(7.25));
        overview.setPegRatio(BigDecimal.valueOf(1.80));

        when(tickerSummaryRepository.findByTickerIgnoreCase("aapl")).thenReturn(Optional.of(summary));
        when(tickerOverviewRepository.findByTickerIgnoreCase("aapl")).thenReturn(Optional.of(overview));

        // When
        Optional<DetailsSummaryResponse> result = stockDetailsService.getStockDetailsSummaryByTicker(ticker);

        // Then
        assertThat(result).isPresent();
        verify(tickerSummaryRepository).findByTickerIgnoreCase("aapl");
        verify(tickerOverviewRepository).findByTickerIgnoreCase("aapl");
    }
}