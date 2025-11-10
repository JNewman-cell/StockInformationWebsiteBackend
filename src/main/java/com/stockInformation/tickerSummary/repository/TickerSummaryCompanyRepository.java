package com.stockInformation.tickerSummary.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.Predicate;
import com.stockInformation.tickerSummary.dto.TickerSummaryDTO;

public interface TickerSummaryCompanyRepository {

    Page<TickerSummaryDTO> findAllWithCompanyName(Predicate predicate, Pageable pageable);
    
}