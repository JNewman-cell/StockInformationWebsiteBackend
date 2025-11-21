package com.stockInformation.stockDetails.transformer;

import org.junit.jupiter.api.Test;

import com.stockInformation.stockDetails.dto.*;
import com.stockInformation.stockDetails.entity.TickerOverview;
import com.stockInformation.tickerSummary.entity.TickerSummary;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class StockDetailsTransformerTest {

    @Test
    void testBuildStockDetailsSummary() {
        // Given
        TickerSummary summary = new TickerSummary("AAPL", BigDecimal.valueOf(150.00));
        summary.setMarketCap(2000000000000L);
        summary.setPeRatio(BigDecimal.valueOf(25.50));
        summary.setForwardPeRatio(BigDecimal.valueOf(22.00));
        summary.setFiftyDayAverage(BigDecimal.valueOf(145.00));
        summary.setTwoHundredDayAverage(BigDecimal.valueOf(140.00));
        summary.setDividendYield(BigDecimal.valueOf(0.82));
        summary.setPayoutRatio(BigDecimal.valueOf(25.00));

        TickerOverview overview = new TickerOverview();
        overview.setTicker("AAPL");
        overview.setEnterpriseToEbitda(BigDecimal.valueOf(18.50));
        overview.setPriceToBook(BigDecimal.valueOf(8.50));
        overview.setGrossMargin(BigDecimal.valueOf(38.50));
        overview.setOperatingMargin(BigDecimal.valueOf(25.30));
        overview.setProfitMargin(BigDecimal.valueOf(20.10));
        overview.setEarningsGrowth(BigDecimal.valueOf(15.70));
        overview.setRevenueGrowth(BigDecimal.valueOf(10.20));
        overview.setTrailingEps(BigDecimal.valueOf(6.15));
        overview.setForwardEps(BigDecimal.valueOf(7.25));
        overview.setPegRatio(BigDecimal.valueOf(1.80));

        // When
        DetailsSummaryResponse response = StockDetailsTransformer.buildStockDetailsSummary(summary, overview);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.valuation()).isNotNull();
        assertThat(response.margin()).isNotNull();
        assertThat(response.growth()).isNotNull();
        assertThat(response.dividend()).isNotNull();

        // Check valuation
        Valuation valuation = response.valuation();
        assertThat(valuation.marketCap()).isEqualTo(2000000000000L);
        assertThat(valuation.peRatio()).isEqualByComparingTo(BigDecimal.valueOf(25.50));
        assertThat(valuation.forwardPeRatio()).isEqualByComparingTo(BigDecimal.valueOf(22.00));
        assertThat(valuation.enterpriseToEbitda()).isEqualByComparingTo(BigDecimal.valueOf(18.50));
        assertThat(valuation.priceToBook()).isEqualByComparingTo(BigDecimal.valueOf(8.50));

        // Check moving averages
        MovingAverage fiftyDay = valuation.fiftyDayAverage();
        assertThat(fiftyDay.movingAverage()).isEqualByComparingTo(BigDecimal.valueOf(145.00));
        assertThat(fiftyDay.percentChangeFromPreviousClose()).isEqualByComparingTo(BigDecimal.valueOf(3.33)); // (150-145)/150 * 100 = 3.33

        MovingAverage twoHundredDay = valuation.twoHundredDayAverage();
        assertThat(twoHundredDay.movingAverage()).isEqualByComparingTo(BigDecimal.valueOf(140.00));
        assertThat(twoHundredDay.percentChangeFromPreviousClose()).isEqualByComparingTo(BigDecimal.valueOf(6.67)); // (150-140)/150 * 100 = 6.67

        // Check margins
        Margins margins = response.margin();
        assertThat(margins.grossMargin()).isEqualByComparingTo(BigDecimal.valueOf(38.50));
        assertThat(margins.operatingMargin()).isEqualByComparingTo(BigDecimal.valueOf(25.30));
        assertThat(margins.profitMargin()).isEqualByComparingTo(BigDecimal.valueOf(20.10));

        // Check growth
        Growth growth = response.growth();
        assertThat(growth.earningsGrowth()).isEqualByComparingTo(BigDecimal.valueOf(15.70));
        assertThat(growth.revenueGrowth()).isEqualByComparingTo(BigDecimal.valueOf(10.20));
        assertThat(growth.trailingEps()).isEqualByComparingTo(BigDecimal.valueOf(6.15));
        assertThat(growth.forwardEps()).isEqualByComparingTo(BigDecimal.valueOf(7.25));
        assertThat(growth.peg()).isEqualByComparingTo(BigDecimal.valueOf(1.80));
        assertThat(growth.forwardEarningsGrowth()).isEqualByComparingTo(BigDecimal.valueOf(17.89)); // (7.25-6.15)/6.15 * 100 ≈ 17.89

        // Check dividend
        Dividend dividend = response.dividend();
        assertThat(dividend.dividendYield()).isEqualByComparingTo(BigDecimal.valueOf(0.82));
        assertThat(dividend.payoutRatio()).isEqualByComparingTo(BigDecimal.valueOf(25.00));
    }

    @Test
    void testBuildStockDetailsSummary_NoDividend() {
        // Given
        TickerSummary summary = new TickerSummary("AAPL", BigDecimal.valueOf(150.00));
        summary.setMarketCap(2000000000000L);
        summary.setFiftyDayAverage(BigDecimal.valueOf(145.00));
        summary.setTwoHundredDayAverage(BigDecimal.valueOf(140.00));
        // No dividend fields set

        TickerOverview overview = new TickerOverview();
        overview.setTicker("AAPL");
        overview.setEarningsGrowth(BigDecimal.valueOf(15.70));
        overview.setRevenueGrowth(BigDecimal.valueOf(10.20));
        overview.setTrailingEps(BigDecimal.valueOf(6.15));
        overview.setForwardEps(BigDecimal.valueOf(7.25));
        overview.setPegRatio(BigDecimal.valueOf(1.80));

        // When
        DetailsSummaryResponse response = StockDetailsTransformer.buildStockDetailsSummary(summary, overview);

        // Then
        assertThat(response.dividend()).isNull();
    }
}