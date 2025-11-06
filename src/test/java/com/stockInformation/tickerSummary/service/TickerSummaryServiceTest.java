package com.stockInformation.cikLookup.service;

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
import com.stockInformation.tickerSummary.repository.TickerSummaryRepository;
import com.stockInformation.tickerSummary.service.TickerSummaryService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        when(tickerSummaryRepository.findByTickerIgnoreCase("AAPL")).thenReturn(Optional.of(tickerSummary));

        // When
        Optional<TickerSummary> result = tickerSummaryService.findByTicker("AAPL");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(tickerSummary);
        verify(tickerSummaryRepository).findByTickerIgnoreCase("AAPL");
    }

    @Test
    void testFindAllWithPageable() {
        // Given
        Pageable pageable = PageRequest.of(0, 20);
        List<TickerSummary> tickerSummaries = Arrays.asList(new TickerSummary("AAPL", new BigDecimal("150.00")));
        Page<TickerSummary> page = new PageImpl<>(tickerSummaries, pageable, 1);
        when(tickerSummaryRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<TickerSummary> result = tickerSummaryService.findAll(pageable);

        // Then
        assertThat(result).isEqualTo(page);
        verify(tickerSummaryRepository).findAll(pageable);
    }
}