package com.stockInformation.stockDetails.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for stock summary response with categorized metrics
 */
@Schema(description = "Stock summary response with valuations, margin, growth, and dividend metrics")
public record DetailsSummaryResponse(

    @Schema(description = "Valuation metrics")
    @NotNull(message = "Valuation is required")
    Valuation valuation,

    @Schema(description = "Margin metrics")
    @NotNull(message = "Margin is required")
    Margins margin,

    @Schema(description = "Growth metrics")
    @NotNull(message = "Growth is required")
    Growth growth,

    @Schema(description = "Dividend metrics")
    Dividend dividend

) {}
