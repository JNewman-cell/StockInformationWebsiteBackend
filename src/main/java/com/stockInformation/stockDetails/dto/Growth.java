package com.stockInformation.stockDetails.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Data Transfer Object for stock growth metrics
 */
@Schema(description = "Stock growth metrics including EPS and growth rates")
public record Growth(

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
    BigDecimal peg

) {}
