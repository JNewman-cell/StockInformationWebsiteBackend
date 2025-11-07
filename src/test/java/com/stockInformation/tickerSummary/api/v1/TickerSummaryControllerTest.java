package com.stockInformation.tickerSummary.api.v1;

import com.stockInformation.tickerSummary.dto.TickerSummaryDTO;
import com.stockInformation.tickerSummary.entity.TickerSummary;
import com.stockInformation.tickerSummary.service.TickerSummaryService;

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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TickerSummaryController.class)
@Import({
    com.stockInformation.tickerSummary.api.v1.TickerSummaryMapperImpl.class,
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
        TickerSummaryDTO dto1 = new TickerSummaryDTO("AAPL", "Apple Inc.", 2000000000000L, new BigDecimal("150.00"), null, null, null, null, null, null);
        TickerSummaryDTO dto2 = new TickerSummaryDTO("MSFT", "Microsoft Corporation", 2000000000000L, new BigDecimal("300.00"), null, null, null, null, null, null);
        List<TickerSummaryDTO> dtos = List.of(dto1, dto2);
        Page<TickerSummaryDTO> page = new PageImpl<>(dtos, PageRequest.of(0, 20), dtos.size());

        when(tickerSummaryService.getPaginatedList(
            any(), any(), any(), any(), any(),
            any(), any(), any(), any(), any(), any(),
            any(), any(), any(), any(), any(), any()
        )).thenReturn(page);

        mockMvc.perform(get("/api/v1/ticker-summary/list")
                .param("page", "0")
                .param("pageSize", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].ticker").value("AAPL"))
                .andExpect(jsonPath("$.content[1].ticker").value("MSFT"));

        verify(tickerSummaryService).getPaginatedList(
            any(), any(), any(), any(), any(),
            any(), any(), any(), any(), any(), any(),
            any(), any(), any(), any(), any(), any()
        );
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
