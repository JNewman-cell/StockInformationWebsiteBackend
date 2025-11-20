package com.stockInformation.stockDetails.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Data Transfer Object for stock valuation metrics
 */
@Schema(description = "Stock valuation metrics including ratios and moving averages")
public record Valuation(

    @Schema(description = "Market capitalization in USD", example = "3000000000000")
    @NotNull(message = "Market cap is required")
    @Min(value = 0, message = "Market cap must be non-negative")
    Long marketCap,

    @Schema(description = "Price-to-Earnings ratio", example = "25.5")
    @DecimalMin(value = "0.0", message = "PE ratio must be non-negative")
    BigDecimal peRatio,

    @Schema(description = "Forward Price-to-Earnings ratio (estimated)", example = "22.3")
    @DecimalMin(value = "0.0", message = "Forward PE ratio must be non-negative")
    BigDecimal forwardPeRatio,

    @Schema(description = "Enterprise value to EBITDA ratio", example = "12.5")
    @DecimalMin(value = "0.0", message = "Enterprise to EBITDA must be non-negative")
    BigDecimal enterpriseToEbitda,

    @Schema(description = "Price-to-book ratio", example = "3.2")
    @DecimalMin(value = "0.0", message = "Price to book must be non-negative")
    BigDecimal priceToBook,

    @Schema(description = "50-day moving average price", example = "145.67")
    @NotNull(message = "50-day average is required")
    @DecimalMin(value = "0.0", message = "50-day average must be non-negative")
    BigDecimal fiftyDayAverage,

    @Schema(description = "200-day moving average price", example = "140.23")
    @NotNull(message = "200-day average is required")
    @DecimalMin(value = "0.0", message = "200-day average must be non-negative")
    BigDecimal twoHundredDayAverage

) {}
