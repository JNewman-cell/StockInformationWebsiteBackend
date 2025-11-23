package com.stockInformation.tickerSummary.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.stockInformation.tickerSummary.entity.TickerSummary;
import com.stockInformation.tickerSummary.dto.TickerSummaryDTO;
import com.stockInformation.tickerSummary.repository.TickerSummaryRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TickerSummaryServiceTest {

    @Mock
    private TickerSummaryRepository tickerSummaryRepository;

    @InjectMocks
    private TickerSummaryService tickerSummaryService;

    @Test
    void testFindByTicker() {
        // Given
        TickerSummary tickerSummary = new TickerSummary("AAPL", new BigDecimal("150.00"));
        when(tickerSummaryRepository.findByTickerIgnoreCase("aapl")).thenReturn(Optional.of(tickerSummary));

        // When
        Optional<TickerSummary> result = tickerSummaryService.findByTicker("AAPL");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(tickerSummary);
        verify(tickerSummaryRepository).findByTickerIgnoreCase("aapl");
    }

    @Test
    void testFindAllWithPageable() {
        // Given
        Pageable pageable = PageRequest.of(0, 20);
        List<TickerSummary> tickerSummaries = List.of(new TickerSummary("AAPL", new BigDecimal("150.00")));
        Objects.requireNonNull(tickerSummaries, "tickerSummaries must not be null for test");
        Page<TickerSummary> page = new PageImpl<>(tickerSummaries, pageable, 1);
        when(tickerSummaryRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<TickerSummary> result = tickerSummaryService.findAll(pageable);

        // Then
        assertThat(result).isEqualTo(page);
        verify(tickerSummaryRepository).findAll(pageable);
    }

    @Test
    void testGetPaginatedListWithAnnualDividendGrowthFilter() {
        // Given
        Pageable pageable = PageRequest.of(0, 20);
        List<TickerSummaryDTO> content = List.of(new TickerSummaryDTO("AAPL", new BigDecimal("150.00")));
        Page<TickerSummaryDTO> page = new PageImpl<>(content, pageable, 1);

        when(tickerSummaryRepository.findAllWithCompanyName(any(), any(Pageable.class))).thenReturn(page);

        // When
        Page<TickerSummaryDTO> result = tickerSummaryService.getPaginatedList(
            null, 0, 20, "ticker", "ASC",
            null, null, null, null, null, null,
            null, null, null, null, null, null,
            new BigDecimal("2.5"), null
        );

        // Then
        assertThat(result).isEqualTo(page);
        verify(tickerSummaryRepository).findAllWithCompanyName(any(), any(Pageable.class));
    }
}