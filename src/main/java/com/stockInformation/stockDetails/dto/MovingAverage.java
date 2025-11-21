package com.stockInformation.stockDetails.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Data Transfer Object for moving average metrics
 */
@Schema(description = "Moving average and percentage change metrics")
public record MovingAverage(

    @Schema(description = "Moving average price", example = "145.67")
    @NotNull(message = "Moving average is required")
    @DecimalMin(value = "0.0", message = "Moving average must be non-negative")
    @DecimalMax(value = "99999999.99", message = "Moving average cannot exceed 99999999.99")
    BigDecimal movingAverage,

    @Schema(description = "Percentage change from previous close", example = "2.5")
    @NotNull(message = "Percent change is required")
    @DecimalMin(value = "-99999.99", message = "Percent change cannot be less than -999.99")
    @DecimalMax(value = "99999.99", message = "Percent change cannot exceed 999.99")
    BigDecimal percentChangeFromPreviousClose

) {
    public static MovingAverage of(BigDecimal movingAverage, BigDecimal previousClose) {
        BigDecimal percentChange = previousClose.subtract(movingAverage).multiply(BigDecimal.valueOf(100)).divide(previousClose, 2, RoundingMode.HALF_UP);
        return new MovingAverage(movingAverage, percentChange);
    }
}
