package com.stockinfo.backend.service;

import com.stockinfo.backend.entity.TickerSummary;
import com.stockinfo.backend.repository.TickerSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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