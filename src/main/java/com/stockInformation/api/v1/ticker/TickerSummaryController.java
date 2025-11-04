package com.stockinfo.backend.api.v1.ticker;

import com.stockinfo.backend.entity.TickerSummary;
import com.stockinfo.backend.service.TickerSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/ticker-summary")
@RequiredArgsConstructor
public class TickerSummaryController {

    private final TickerSummaryService tickerSummaryService;
    private final TickerSummaryMapper tickerSummaryMapper;

    /**
     * Get all ticker summaries with pagination
     */
    @GetMapping
    public ResponseEntity<Page<TickerSummaryDTO>> getAllTickerSummaries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TickerSummary> tickerSummaries = tickerSummaryService.findAll(pageable);
        Page<TickerSummaryDTO> dtos = tickerSummaries.map(tickerSummaryMapper::toDTO);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Get ticker summary by ticker symbol
     */
    @GetMapping("/{ticker}")
    public ResponseEntity<TickerSummaryDTO> getTickerSummaryByTicker(@PathVariable String ticker) {
        Optional<TickerSummary> tickerSummary = tickerSummaryService.findByTicker(ticker);
        return tickerSummary
                .map(tickerSummaryMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
