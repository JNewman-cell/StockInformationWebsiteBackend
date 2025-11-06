package com.stockInformation.cikLookup.api.v1.ticker;

import com.stockInformation.tickerSummary.entity.TickerSummary;
import com.stockInformation.tickerSummary.service.TickerSummaryService;
import com.stockInformation.tickerSummary.api.v1.TickerSummaryController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TickerSummaryController.class)
@Import({
    com.stockInformation.db.api.v1.ticker.TickerSummaryMapperImpl.class,
    com.stockInformation.config.SecurityConfig.class
})
class TickerSummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TickerSummaryService tickerSummaryService;

    @Test
    @WithMockUser
    void testGetAllTickerSummaries() throws Exception {
        TickerSummary ticker1 = new TickerSummary("AAPL", new BigDecimal("150.00"));
        TickerSummary ticker2 = new TickerSummary("MSFT", new BigDecimal("300.00"));
        List<TickerSummary> tickers = Arrays.asList(ticker1, ticker2);
        Page<TickerSummary> page = new PageImpl<>(tickers, PageRequest.of(0, 20), tickers.size());

        when(tickerSummaryService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/ticker-summary")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2));

        verify(tickerSummaryService).findAll(any(Pageable.class));
    }

    @Test
    @WithMockUser
    void testGetTickerSummaryByTicker() throws Exception {
        TickerSummary ticker = new TickerSummary("AAPL", new BigDecimal("150.00"));

        when(tickerSummaryService.findByTicker("AAPL")).thenReturn(Optional.of(ticker));

        mockMvc.perform(get("/api/v1/ticker-summary/AAPL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticker").value("AAPL"))
                .andExpect(jsonPath("$.previousClose").value(150.00));

        verify(tickerSummaryService).findByTicker("AAPL");
    }

    @Test
    @WithMockUser
    void testGetTickerSummaryByTickerNotFound() throws Exception {
        when(tickerSummaryService.findByTicker("INVALID")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/ticker-summary/INVALID"))
                .andExpect(status().isNotFound());

        verify(tickerSummaryService).findByTicker("INVALID");
    }
}
