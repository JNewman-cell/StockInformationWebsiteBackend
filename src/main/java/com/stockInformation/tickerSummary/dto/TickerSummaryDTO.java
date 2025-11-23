package com.stockInformation.tickerSummary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Data Transfer Object for TickerSummary API responses and requests
 */
@Schema(description = "Stock ticker summary with financial metrics and company information")
public record TickerSummaryDTO(

    @Schema(description = "Stock ticker symbol", example = "AAPL", maxLength = 20)
    @NotBlank(message = "Ticker symbol is required")
    @Size(max = 20, message = "Ticker symbol cannot exceed 20 characters")
    String ticker,

    @Schema(description = "Company name associated with the ticker", example = "Apple Inc.")
    String companyName,

    @Schema(description = "Market capitalization in USD", example = "3000000000000")
    @Min(value = 0, message = "Market cap must be non-negative")
    Long marketCap,

    @Schema(description = "Previous day's closing price", example = "150.25")
    @NotNull(message = "Previous close is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Previous close must be positive")
    BigDecimal previousClose,

    @Schema(description = "Price-to-Earnings ratio", example = "25.5")
    @DecimalMin(value = "0.0", message = "PE ratio must be non-negative")
    BigDecimal peRatio,

    @Schema(description = "Forward Price-to-Earnings ratio (estimated)", example = "22.3")
    @DecimalMin(value = "0.0", message = "Forward PE ratio must be non-negative")
    BigDecimal forwardPeRatio,

    @Schema(description = "Dividend yield as percentage", example = "0.82", maximum = "99.99")
    @DecimalMin(value = "0.0", message = "Dividend yield must be non-negative")
    @DecimalMax(value = "99.99", message = "Dividend yield cannot exceed 99.99%")
    BigDecimal dividendYield,

    @Schema(description = "Dividend payout ratio as percentage", example = "25.5", maximum = "99.99")
    @DecimalMin(value = "0.0", message = "Payout ratio must be non-negative")
    @DecimalMax(value = "99.99", message = "Payout ratio cannot exceed 99.99%")
    BigDecimal payoutRatio,

    @Schema(description = "Annual dividend growth as percentage", example = "5.25", maximum = "999.99")
    @DecimalMin(value = "0.0", message = "Annual dividend growth must be non-negative")
    @DecimalMax(value = "999.99", message = "Annual dividend growth cannot exceed 999.99%")
    BigDecimal annualDividendGrowth,

    @Schema(description = "Five-year average dividend yield as percentage", example = "2.05", maximum = "999.99")
    @DecimalMin(value = "0.0", message = "Five-year average dividend yield must be non-negative")
    @DecimalMax(value = "999.99", message = "Five-year average dividend yield cannot exceed 999.99%")
    BigDecimal fiveYearAvgDividendYield,

    @Schema(description = "50-day moving average price", example = "145.67")
    @DecimalMin(value = "0.0", message = "50-day average must be non-negative")
    BigDecimal fiftyDayAverage,

    @Schema(description = "200-day moving average price", example = "140.23")
    @DecimalMin(value = "0.0", message = "200-day average must be non-negative")
    BigDecimal twoHundredDayAverage

) {

    // Custom constructor for test convenience
    public TickerSummaryDTO(String ticker, BigDecimal previousClose) {
        this(ticker, null, null, previousClose, null, null, null, null, null, null, null, null);
    }
}
