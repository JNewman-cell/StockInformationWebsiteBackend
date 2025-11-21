package com.stockInformation.search.api.v1;

import com.stockInformation.search.dto.AutocompleteResponse;
import com.stockInformation.search.dto.AutocompleteResult;
import com.stockInformation.search.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SearchController.class)
@AutoConfigureMockMvc(addFilters = false)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SearchService searchService;

    @Test
    void testSearchTickerSummaries() throws Exception {
        // Given
        String query = "apple";
        List<AutocompleteResult> results = Arrays.asList(
            new AutocompleteResult("AAPL", "Apple Inc.", 0.99)
        );
        AutocompleteResponse response = new AutocompleteResponse(query, results);
        when(searchService.autocomplete(query)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/v1/search/auto-complete")
                .param("query", query))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.query").value(query))
                .andExpect(jsonPath("$.results[0].symbol").value("AAPL"))
                .andExpect(jsonPath("$.results[0].name").value("Apple Inc."))
                .andExpect(jsonPath("$.results[0].score").value(0.99));

        verify(searchService).autocomplete(query);
    }

    @Test
    void testSearchTickerSummariesEmptyResults() throws Exception {
        // Given
        String query = "nonexistent";
        List<AutocompleteResult> results = Arrays.asList();
        AutocompleteResponse response = new AutocompleteResponse(query, results);
        when(searchService.autocomplete(query)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/v1/search/auto-complete")
                .param("query", query))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.query").value(query))
                .andExpect(jsonPath("$.results").isEmpty());

        verify(searchService).autocomplete(query);
    }
}