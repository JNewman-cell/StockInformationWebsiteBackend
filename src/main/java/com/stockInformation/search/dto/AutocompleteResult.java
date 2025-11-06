package com.stockInformation.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Autocomplete result for autocomplete search", example = """
        {
            symbol: "AAPL", 
            name: "Apple Inc.", 
            score = "0.92"
        }
        """)
public record AutocompleteResult(

    @Schema(description = "Ticker symbol of a company", example = "AAPL")
    @NotNull(message = "Ticker symbol is required")
    String symbol,

    @Schema(description = "Name of a company", example = "Apple Inc.")
    @NotNull(message = "Ticker symbol is required")
    String name,

    @Schema(description = "Score of a search result", example = "0.92")
    @NotNull(message = "Score is required")
    Double score
) {}   