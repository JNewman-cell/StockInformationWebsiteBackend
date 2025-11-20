package com.stockInformation.stockDetails.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Data Transfer Object for stock margin metrics
 */
@Schema(description = "Stock margin metrics including gross, operating, and profit margins")
public record Margins(

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
    BigDecimal profitMargin

) {}
