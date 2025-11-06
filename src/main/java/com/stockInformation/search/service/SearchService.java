package com.stockInformation.search.service;

import com.stockInformation.search.dto.AutocompleteResult;
import com.stockInformation.search.dto.AutocompleteResponse;
import com.stockInformation.search.repository.SearchRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public AutocompleteResponse autocomplete(String query) {
        // Lowercase the input for the SQL query which expects lowercase comparisons
        List<AutocompleteResult> results = searchRepository.searchByInputIgnoreCase(query.toLowerCase());
        return new AutocompleteResponse(query, results);
    }
}