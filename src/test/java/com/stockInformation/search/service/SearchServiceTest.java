package com.stockInformation.search.service;

import com.stockInformation.search.dto.AutocompleteResult;
import com.stockInformation.search.dto.AutocompleteResponse;
import com.stockInformation.search.repository.SearchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private SearchRepository searchRepository;

    @InjectMocks
    private SearchService searchService;

    @Test
    void testAutocomplete() {
        // Given
        String query = "Apple";
        List<AutocompleteResult> results = Arrays.asList(
            new AutocompleteResult("AAPL", "Apple Inc.", 0.99)
        );
        when(searchRepository.searchByInputIgnoreCase("apple")).thenReturn(results);

        // When
        AutocompleteResponse response = searchService.autocomplete(query);

        // Then
        assertThat(response.query()).isEqualTo(query);
        assertThat(response.results()).isEqualTo(results);
        verify(searchRepository).searchByInputIgnoreCase("apple");
    }

    @Test
    void testAutocompleteWithSpecialChars() {
        // Given
        String query = "Apple Inc.";
        List<AutocompleteResult> results = Arrays.asList(
            new AutocompleteResult("AAPL", "Apple Inc.", 0.99)
        );
        when(searchRepository.searchByInputIgnoreCase("apple")).thenReturn(results);

        // When
        AutocompleteResponse response = searchService.autocomplete(query);

        // Then
        assertThat(response.query()).isEqualTo(query);
        assertThat(response.results()).isEqualTo(results);
        verify(searchRepository).searchByInputIgnoreCase("apple");
    }
}
