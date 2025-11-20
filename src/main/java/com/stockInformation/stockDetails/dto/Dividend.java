package com.stockInformation.stockDetails.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Data Transfer Object for stock dividend metrics
 */
@Schema(description = "Stock dividend metrics including yield and payout ratio")
public record Dividend(

    @Schema(description = "Dividend yield as percentage", example = "0.82", maximum = "999.99")
    @DecimalMin(value = "0.0", message = "Dividend yield must be non-negative")
    @DecimalMax(value = "999.99", message = "Dividend yield cannot exceed 999.99%")
    BigDecimal dividendYield,

    @Schema(description = "Dividend payout ratio as percentage", example = "25.5", maximum = "999.99")
    @DecimalMin(value = "0.0", message = "Payout ratio must be non-negative")
    @DecimalMax(value = "999.99", message = "Payout ratio cannot exceed 999.99%")
    BigDecimal payoutRatio

) {}
