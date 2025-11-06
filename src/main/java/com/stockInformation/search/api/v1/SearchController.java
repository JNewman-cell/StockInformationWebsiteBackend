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
     * Search by company name and ticker symbol
     * Query Parameters:
     * Search Input: The keyword to search for in company names and ticker symbols.
     */
    @GetMapping("/auto-complete")
    public ResponseEntity<AutocompleteResponse> searchTickerSummaries(@RequestParam String searchInput) {
        // Delegate creation of the AutocompleteResponse to the service layer
        AutocompleteResponse response = searchService.autocomplete(searchInput);
        return ResponseEntity.ok(response);
    }
}