package com.stockInformation.stockDetails.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Data Transfer Object for TickerSummary API responses and requests
 */
@Schema(description = "Stock ticker summary with financial metrics and company information")
public record DetailsSummaryDTO(

    @Schema(description = "Stock ticker symbol", example = "AAPL", maxLength = 20)
    @NotBlank(message = "Ticker symbol is required")
    @Size(max = 20, message = "Ticker symbol cannot exceed 20 characters")
    String ticker,

    @Schema(description = "Company name associated with the ticker", example = "Apple Inc.")
    String companyName,

    @Schema(description = "Market capitalization in USD", example = "3000000000000")
    @NotNull(message = "Market cap is required")
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

    @Schema(description = "Dividend yield as percentage", example = "0.82", maximum = "999.99")
    @DecimalMin(value = "0.0", message = "Dividend yield must be non-negative")
    @DecimalMax(value = "999.99", message = "Dividend yield cannot exceed 999.99%")
    BigDecimal dividendYield,

    @Schema(description = "Dividend payout ratio as percentage", example = "25.5", maximum = "999.99")
    @DecimalMin(value = "0.0", message = "Payout ratio must be non-negative")
    @DecimalMax(value = "999.99", message = "Payout ratio cannot exceed 999.99%")
    BigDecimal payoutRatio,

    @Schema(description = "50-day moving average price", example = "145.67")
    @NotNull(message = "50-day average is required")
    @DecimalMin(value = "0.0", message = "50-day average must be non-negative")
    BigDecimal fiftyDayAverage,

    @Schema(description = "200-day moving average price", example = "140.23")
    @NotNull(message = "200-day average is required")
    @DecimalMin(value = "0.0", message = "200-day average must be non-negative")
    BigDecimal twoHundredDayAverage,

    @Schema(description = "Enterprise value to EBITDA ratio", example = "12.5")
    @DecimalMin(value = "0.0", message = "Enterprise to EBITDA must be non-negative")
    BigDecimal enterpriseToEbitda,

    @Schema(description = "Price-to-book ratio", example = "3.2")
    @DecimalMin(value = "0.0", message = "Price to book must be non-negative")
    BigDecimal priceToBook,

    @Schema(description = "Gross profit margin as percentage", example = "38.5", maximum = "999.99")
    @DecimalMin(value = "0.0", message = "Gross margin must be non-negative")
    @DecimalMax(value = "999.99", message = "Gross margin cannot exceed 999.99%")
    BigDecimal grossMargin,

    @Schema(description = "Operating profit margin as percentage", example = "25.3", maximum = "999.99")
    @DecimalMin(value = "0.0", message = "Operating margin must be non-negative")
    @DecimalMax(value = "999.99", message = "Operating margin cannot exceed 999.99%")
    BigDecimal operatingMargin,

    @Schema(description = "Net profit margin as percentage", example = "20.1", maximum = "999.99")
    @DecimalMin(value = "0.0", message = "Profit margin must be non-negative")
    @DecimalMax(value = "999.99", message = "Profit margin cannot exceed 999.99%")
    BigDecimal profitMargin,

    @Schema(description = "Earnings per share growth rate", example = "15.7")
    BigDecimal earningsGrowth,

    @Schema(description = "Revenue growth rate", example = "10.2")
    BigDecimal revenueGrowth,

    @Schema(description = "Trailing earnings per share", example = "6.15")
    BigDecimal trailingEps,

    @Schema(description = "Forward earnings per share (estimated)", example = "7.25")
    BigDecimal forwardEps,

    @Schema(description = "Price-to-earnings growth ratio", example = "1.8")
    @DecimalMin(value = "0.0", message = "PEG ratio must be non-negative")
    BigDecimal pegRatio

) {

    // Custom constructor for test convenience
    public DetailsSummaryDTO(String ticker, BigDecimal previousClose) {
        this(ticker, null, null, previousClose, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
}
