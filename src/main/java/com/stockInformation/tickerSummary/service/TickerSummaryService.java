package com.stockInformation.tickerSummary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stockInformation.tickerSummary.entity.TickerSummary;
import com.stockInformation.tickerSummary.repository.TickerSummaryRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TickerSummaryService {

    private final TickerSummaryRepository tickerSummaryRepository;

    /**
     * Find ticker summary by ticker symbol (case-insensitive)
     */
    @Transactional(readOnly = true)
    public Optional<TickerSummary> findByTicker(String ticker) {
        return tickerSummaryRepository.findByTickerIgnoreCase(ticker);
    }

    /**
     * Get all ticker summaries with pagination
     */
    @Transactional(readOnly = true)
    public Page<TickerSummary> findAll(Pageable pageable) {
        return tickerSummaryRepository.findAll(pageable);
    }
}