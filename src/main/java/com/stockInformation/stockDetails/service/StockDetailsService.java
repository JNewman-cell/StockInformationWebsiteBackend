package com.stockInformation.stockDetails.service;

import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import com.stockInformation.stockDetails.dto.DetailsSummaryResponse;
import com.stockInformation.stockDetails.transformer.StockDetailsTransformer;
import com.stockInformation.stockDetails.entity.TickerOverview;
import com.stockInformation.stockDetails.repository.TickerOverviewRepository;
import com.stockInformation.tickerSummary.entity.TickerSummary;
import com.stockInformation.tickerSummary.repository.TickerSummaryRepository;


@Service
@RequiredArgsConstructor
public class StockDetailsService {
    private final TickerSummaryRepository tickerSummaryRepository;
    private final TickerOverviewRepository tickerOverviewRepository;
    
    @Transactional(readOnly = true)
    @Cacheable(value = "stockdetailssummary", key = "#ticker == null ? '' : #ticker.toLowerCase()")
    public Optional<DetailsSummaryResponse> getStockDetailsSummaryByTicker(String ticker){
        String lowercaseTicker = ticker.toLowerCase();

        Optional<TickerOverview> overviewOpt = tickerOverviewRepository.findByTickerIgnoreCase(lowercaseTicker);
        Optional<TickerSummary> summaryOpt = tickerSummaryRepository.findByTickerIgnoreCase(lowercaseTicker);

        if (overviewOpt.isEmpty() || summaryOpt.isEmpty()) {
            return Optional.empty();
        }

        DetailsSummaryResponse response = StockDetailsTransformer.buildStockDetailsSummary(summaryOpt.get(), overviewOpt.get());

        return Optional.of(response);
    }
}
