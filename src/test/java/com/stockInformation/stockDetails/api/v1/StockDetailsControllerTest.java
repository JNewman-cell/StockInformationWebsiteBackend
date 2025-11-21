package com.stockInformation.stockDetails.api.v1;

import com.stockInformation.stockDetails.dto.DetailsSummaryResponse;
import com.stockInformation.stockDetails.service.StockDetailsService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StockDetailsController.class)
@AutoConfigureMockMvc(addFilters = false)
class StockDetailsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StockDetailsService stockDetailsService;

    @Test
    void testGetStockDetailsSummaryByTicker_ValidTicker_ReturnsOk() throws Exception {
        // Given
        String ticker = "AAPL";
        DetailsSummaryResponse response = new DetailsSummaryResponse(null, null, null, null);
        when(stockDetailsService.getStockDetailsSummaryByTicker(ticker)).thenReturn(Optional.of(response));

        // When & Then
        mockMvc.perform(get("/api/v1/stock-details/summary/{ticker}", ticker))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        verify(stockDetailsService).getStockDetailsSummaryByTicker(ticker);
    }

    @Test
    void testGetStockDetailsSummaryByTicker_InvalidTicker_ReturnsNotFound() throws Exception {
        // Given
        String ticker = "INVALID";
        when(stockDetailsService.getStockDetailsSummaryByTicker(ticker)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/stock-details/summary/{ticker}", ticker))
                .andExpect(status().isNotFound());

        verify(stockDetailsService).getStockDetailsSummaryByTicker(ticker);
    }

    @Test
    void testGetStockDetailsSummaryByTicker_NullTicker_ReturnsNotFound() throws Exception {
        // Given
        String ticker = "NULL";
        when(stockDetailsService.getStockDetailsSummaryByTicker(ticker)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/stock-details/summary/{ticker}", ticker))
                .andExpect(status().isNotFound());

        verify(stockDetailsService).getStockDetailsSummaryByTicker(ticker);
    }

    @Test
    void testGetStockDetailsSummaryByTicker_NotFound_ReturnsNotFound() throws Exception {
        // Given
        String ticker = "INVALID";
        when(stockDetailsService.getStockDetailsSummaryByTicker(ticker)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/stock-details/summary/{ticker}", ticker))
                .andExpect(status().isNotFound());

        verify(stockDetailsService).getStockDetailsSummaryByTicker(ticker);
    }
}