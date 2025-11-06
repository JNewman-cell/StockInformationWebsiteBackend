package com.stockInformation.search.api.v1;

import com.stockInformation.search.dto.AutocompleteResponse;
import com.stockInformation.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    /**
     * Search by company name and ticker symbol.
     * Query Parameters:
     *  - query: the keyword to search for in company names and ticker symbols
     *    (e.g. /api/v1/search/auto-complete?query=apple+inc)
     *
     * The controller forwards the raw query to the service which handles
     * normalization (trimming/lowercasing) and the search logic.
     */
    @GetMapping("/auto-complete")
    public ResponseEntity<AutocompleteResponse> searchTickerSummaries(@RequestParam String query) {
        // Delegate creation of the AutocompleteResponse to the service layer
        AutocompleteResponse response = searchService.autocomplete(query);
        return ResponseEntity.ok(response);
    }
}