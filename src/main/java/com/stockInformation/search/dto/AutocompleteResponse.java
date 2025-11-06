package com.stockInformation.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "Autocomplete result for autocomplete search", example = """
{
    "query": "aap",
    "results": [
        { "symbol": "AAPL",  "name": "Apple Inc.",               "score": 0.99 },
        { "symbol": "AAP",   "name": "Advance Auto Parts, Inc.", "score": 0.72 },
        { "symbol": "AAPLW", "name": "Apple Inc. - Warrants",    "score": 0.48 }
    ]
}
""")
public record AutocompleteResponse(
    
    @Schema(description = "Query used for the autocomplete", example = "AAP")
    @NotNull(message = "Query is required")
    String query,

        @Schema(description = "List of autocomplete results", example = """
        [
            { "symbol": "AAPL",  "name": "Apple Inc.",               "score": 0.99 },
            { "symbol": "AAP",   "name": "Advance Auto Parts, Inc.", "score": 0.72 },
            { "symbol": "AAPLW", "name": "Apple Inc. - Warrants",    "score": 0.48 }
        ]
        """)
    @NotNull(message = "List of autocomplete results is required")
    List<AutocompleteResult> results
){}
