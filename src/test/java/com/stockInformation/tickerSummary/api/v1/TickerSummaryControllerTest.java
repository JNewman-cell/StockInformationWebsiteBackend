package com.stockInformation.tickerSummary.api.v1;

import com.stockInformation.tickerSummary.dto.TickerSummaryDTO;
import com.stockInformation.tickerSummary.service.TickerSummaryService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TickerSummaryController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({
    com.stockInformation.tickerSummary.api.v1.TickerSummaryMapperImpl.class
})
class TickerSummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TickerSummaryService tickerSummaryService;

    @Test
    void testGetAllTickerSummaries() throws Exception {
        TickerSummaryDTO dto1 = new TickerSummaryDTO("AAPL", "Apple Inc.", 2000000000000L, new BigDecimal("150.00"), null, null, null, null, null, null, null, null);
        TickerSummaryDTO dto2 = new TickerSummaryDTO("MSFT", "Microsoft Corporation", 2000000000000L, new BigDecimal("300.00"), null, null, null, null, null, null, null, null);
        List<TickerSummaryDTO> dtos = List.of(dto1, dto2);
        Page<TickerSummaryDTO> page = new PageImpl<>(dtos, PageRequest.of(0, 20), dtos.size());

        when(tickerSummaryService.getPaginatedList(
            any(), any(), any(), any(), any(),
            any(), any(), any(), any(), any(), any(),
            any(), any(), any(), any(), any(), any(), any(), any()
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
            any(), any(), any(), any(), any(), any(), any(), any()
        );
    }

    @Test
    void testGetTickerSummaryByTicker() throws Exception {
        TickerSummaryDTO tickerDTO = new TickerSummaryDTO("AAPL", new BigDecimal("150.00"));

        when(tickerSummaryService.findDTOByTicker("AAPL")).thenReturn(Optional.of(tickerDTO));

        mockMvc.perform(get("/api/v1/ticker-summary/AAPL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticker").value("AAPL"))
                .andExpect(jsonPath("$.previousClose").value(150.00));

        verify(tickerSummaryService).findDTOByTicker("AAPL");
    }

    @Test
    void testGetTickerSummaryByTickerNotFound() throws Exception {
        when(tickerSummaryService.findDTOByTicker("INVALID")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/ticker-summary/INVALID"))
                .andExpect(status().isNotFound());

        verify(tickerSummaryService).findDTOByTicker("INVALID");
    }

    @Test
    void testGetTickerSummaryPaginatedListInvalidPage() throws Exception {
        mockMvc.perform(get("/api/v1/ticker-summary/list")
                .param("page", "-1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tickerSummaryService);
    }

    @Test
    void testGetTickerSummaryPaginatedListInvalidPageSize() throws Exception {
        mockMvc.perform(get("/api/v1/ticker-summary/list")
                .param("pageSize", "100"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tickerSummaryService);
    }

    @Test
    void testGetTickerSummaryPaginatedListInvalidSortOrder() throws Exception {
        mockMvc.perform(get("/api/v1/ticker-summary/list")
                .param("sortOrder", "INVALID"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tickerSummaryService);
    }

    @Test
    void testGetTickerSummaryPaginatedListInvalidSortBy() throws Exception {
        mockMvc.perform(get("/api/v1/ticker-summary/list")
                .param("sortBy", "invalid_field"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tickerSummaryService);
    }

    @Test
    void testGetTickerSummaryPaginatedListInvalidMinMarketCap() throws Exception {
        mockMvc.perform(get("/api/v1/ticker-summary/list")
                .param("minMarketCap", "0"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tickerSummaryService);
    }

    @Test
    void testGetTickerSummaryPaginatedListInvalidMaxMarketCap() throws Exception {
        mockMvc.perform(get("/api/v1/ticker-summary/list")
                .param("maxMarketCap", "-1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tickerSummaryService);
    }

    @Test
    void testGetTickerSummaryPaginatedListInvalidMinPreviousClose() throws Exception {
        mockMvc.perform(get("/api/v1/ticker-summary/list")
                .param("minPreviousClose", "0"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tickerSummaryService);
    }

    @Test
    void testGetTickerSummaryPaginatedListInvalidMaxPreviousClose() throws Exception {
        mockMvc.perform(get("/api/v1/ticker-summary/list")
                .param("maxPreviousClose", "-1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tickerSummaryService);
    }

    @Test
    void testGetTickerSummaryPaginatedListInvalidMinDividendYield() throws Exception {
        mockMvc.perform(get("/api/v1/ticker-summary/list")
                .param("minDividendYield", "-0.01"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tickerSummaryService);
    }

    @Test
    void testGetTickerSummaryPaginatedListInvalidMaxDividendYield() throws Exception {
        mockMvc.perform(get("/api/v1/ticker-summary/list")
                .param("maxDividendYield", "1000"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tickerSummaryService);
    }

    @Test
    void testGetTickerSummaryPaginatedListInvalidMinPayoutRatio() throws Exception {
        mockMvc.perform(get("/api/v1/ticker-summary/list")
                .param("minPayoutRatio", "-0.01"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tickerSummaryService);
    }

    @Test
    void testGetTickerSummaryPaginatedListInvalidMaxPayoutRatio() throws Exception {
        mockMvc.perform(get("/api/v1/ticker-summary/list")
                .param("maxPayoutRatio", "1000"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tickerSummaryService);
    }

    @Test
    void testGetTickerSummaryPaginatedListInvalidMinAnnualDividendGrowth() throws Exception {
        mockMvc.perform(get("/api/v1/ticker-summary/list")
                .param("minAnnualDividendGrowth", "-0.01"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tickerSummaryService);
    }

    @Test
    void testGetTickerSummaryPaginatedListInvalidMaxAnnualDividendGrowth() throws Exception {
        mockMvc.perform(get("/api/v1/ticker-summary/list")
                .param("maxAnnualDividendGrowth", "1000"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tickerSummaryService);
    }
}
