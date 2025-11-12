package com.stockInformation.tickerSummary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.stockInformation.tickerSummary.dto.TickerSummaryDTO;
import com.stockInformation.tickerSummary.entity.QTickerSummary;
import com.stockInformation.tickerSummary.entity.TickerSummary;
import com.stockInformation.tickerSummary.repository.TickerSummaryRepository;

import java.util.Optional;
import java.math.BigDecimal;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class TickerSummaryService {

    private final TickerSummaryRepository tickerSummaryRepository;

    // Controller is responsible for validating allowed sort fields; service trusts controller inputs.

    /**
     * Find ticker summary by ticker symbol (case-insensitive)
     */
    @Transactional(readOnly = true)
    public Optional<TickerSummary> findByTicker(String ticker) {
        return tickerSummaryRepository.findByTickerIgnoreCase(ticker.toLowerCase());
    }

    /**
     * Find ticker summary DTO by ticker symbol (case-insensitive) with company name joined
     */
    @Transactional(readOnly = true)
    public Optional<TickerSummaryDTO> findDTOByTicker(String ticker) {
        return tickerSummaryRepository.findByTickerWithCompanyName(ticker.toLowerCase());
    }

    /**
     * Build a dynamic predicate from provided filters and return a paginated Page<TickerSummaryDTO>.
     * Uses Querydsl for optimized DTO projection with company name.
     */
    @Transactional(readOnly = true)
    public Page<TickerSummaryDTO> getPaginatedList(
            String query,
            Integer page,
            Integer pageSize,
            String sortBy,
            String sortOrder,
            BigDecimal minPreviousClose,
            BigDecimal maxPreviousClose,
            BigDecimal minPe,
            BigDecimal maxPe,
            BigDecimal minForwardPe,
            BigDecimal maxForwardPe,
            BigDecimal minDividendYield,
            BigDecimal maxDividendYield,
            Long minMarketCap,
            Long maxMarketCap,
            BigDecimal minPayoutRatio,
            BigDecimal maxPayoutRatio
    ) {
        // Defensive null-checks to satisfy null-safety expectations from callers
        Objects.requireNonNull(page, "page must not be null");
        Objects.requireNonNull(pageSize, "pageSize must not be null");
        Objects.requireNonNull(sortBy, "sortBy must not be null");
        Objects.requireNonNull(sortOrder, "sortOrder must not be null");

        QTickerSummary t = QTickerSummary.tickerSummary;

        // Build dynamic predicates (business logic)
        BooleanBuilder predicates = new BooleanBuilder();

        if (query != null && !query.isBlank()) {
            predicates.and(t.ticker.containsIgnoreCase(query.trim()));
        }

        if (minPreviousClose != null) {
            predicates.and(t.previousClose.goe(minPreviousClose));
        }
        if (maxPreviousClose != null) {
            predicates.and(t.previousClose.loe(maxPreviousClose));
        }

        if (minPe != null) {
            predicates.and(t.peRatio.goe(minPe));
        }
        if (maxPe != null) {
            predicates.and(t.peRatio.loe(maxPe));
        }

        if (minForwardPe != null) {
            predicates.and(t.forwardPeRatio.goe(minForwardPe));
        }
        if (maxForwardPe != null) {
            predicates.and(t.forwardPeRatio.loe(maxForwardPe));
        }

        if (minDividendYield != null) {
            predicates.and(t.dividendYield.goe(minDividendYield));
        }
        if (maxDividendYield != null) {
            predicates.and(t.dividendYield.loe(maxDividendYield));
        }

        if (minMarketCap != null) {
            predicates.and(t.marketCap.goe(minMarketCap));
        }
        if (maxMarketCap != null) {
            predicates.and(t.marketCap.loe(maxMarketCap));
        }

        if (minPayoutRatio != null) {
            predicates.and(t.payoutRatio.goe(minPayoutRatio));
        }
        if (maxPayoutRatio != null) {
            predicates.and(t.payoutRatio.loe(maxPayoutRatio));
        }

        // Build pageable with sort
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        return tickerSummaryRepository.findAllWithCompanyName(predicates, pageable);
    }

    /**
     * Get all ticker summaries with pagination
     */
    @Transactional(readOnly = true)
    public Page<TickerSummary> findAll(Pageable pageable) {
        Objects.requireNonNull(pageable, "pageable must not be null");
        return tickerSummaryRepository.findAll(pageable);
    }
}