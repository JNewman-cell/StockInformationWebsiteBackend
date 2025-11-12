package com.stockInformation.search.service;

import com.stockInformation.search.dto.AutocompleteResult;
import com.stockInformation.search.dto.AutocompleteResponse;
import com.stockInformation.search.repository.SearchRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.stockInformation.search.utils.utils;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {
    private final SearchRepository searchRepository;

    /**
     * Autocomplete search for ticker symbols and company names.
     *
     * Contract:
     *  - Input: original query string
     *  - Output: AutocompleteResponse containing the original query and a list of AutocompleteResult
     *  - Error modes: returns empty results list if nothing found
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "autocomplete", key = "#query.toLowerCase()")
    public AutocompleteResponse autocomplete(String query) {
        // Handle null/blank queries quickly
        if (query == null || query.isBlank()) {
            return new AutocompleteResponse(query, List.of());
        }

        // Lowercase the input for the SQL query which expects lowercase comparisons
        String lowered = query.toLowerCase();

        // Sanitize the query for SQL pattern matching (moved to utils)
        String processed = utils.sanitizeQueryForSql(lowered);

        List<AutocompleteResult> results = searchRepository.searchByInputIgnoreCase(processed);
        return new AutocompleteResponse(query, results);
    }
}