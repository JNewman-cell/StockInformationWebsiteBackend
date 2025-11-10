package com.stockInformation.tickerSummary.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stockInformation.tickerSummary.dto.TickerSummaryDTO;
import com.stockInformation.tickerSummary.entity.TickerSummary;
import com.stockInformation.tickerSummary.service.TickerSummaryService;
import com.stockInformation.common.dto.PageResponse;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/ticker-summary")
@RequiredArgsConstructor
public class TickerSummaryController {

    private final TickerSummaryService tickerSummaryService;
    private final TickerSummaryMapper tickerSummaryMapper;
    private static final Set<Integer> PAGE_SIZES = Set.of(10, 25, 50);
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
        "ticker", "company_name", "previous_close",
        "pe", "forward_pe", "dividend_yield",
        "market_cap", "payout_ratio"
    );
    private static final Set<String> SORT_DIRECTIONS = Set.of(
        "ASC", "DESC"
    );
    
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

    /**
     * Get paginated and filtered list of ticker summaries
     */
    @GetMapping("/list")
    public ResponseEntity<PageResponse<TickerSummaryDTO>> getTickerSummaryPaginatedList(
        @RequestParam(required = false) String query,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "25") Integer pageSize,
        @RequestParam(defaultValue = "ticker") String sortBy,
        @RequestParam(defaultValue = "ASC") String sortOrder,
        @RequestParam(required = false) BigDecimal minPreviousClose,
        @RequestParam(required = false) BigDecimal maxPreviousClose,
        @RequestParam(required = false) BigDecimal minPe,
        @RequestParam(required = false) BigDecimal maxPe,
        @RequestParam(required = false) BigDecimal minForwardPe,
        @RequestParam(required = false) BigDecimal maxForwardPe,
        @RequestParam(required = false) BigDecimal minDividendYield,
        @RequestParam(required = false) BigDecimal maxDividendYield,
        @RequestParam(required = false) Long minMarketCap,
        @RequestParam(required = false) Long maxMarketCap,
        @RequestParam(required = false) BigDecimal minPayoutRatio,
        @RequestParam(required = false) BigDecimal maxPayoutRatio
    ) {
        // Validate controller-level inputs (controller enforces defaults and bounds)
        if (page < 0) {
            return ResponseEntity.badRequest().build();
        }
        if (!PAGE_SIZES.contains(pageSize)) {
            return ResponseEntity.badRequest().build();
        }
        // Validate sortOrder (must be ASC or DESC)
        if (!SORT_DIRECTIONS.contains(sortOrder)) {
            return ResponseEntity.badRequest().build();
        }

        // Validate sortBy: allow "ticker" or any enum value (case/format tolerant)
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            return ResponseEntity.badRequest().build();
        }

        Page<TickerSummaryDTO> dtos = tickerSummaryService.getPaginatedList(
            query, page, pageSize, sortBy, sortOrder,
            minPreviousClose, maxPreviousClose,
            minPe, maxPe,
            minForwardPe, maxForwardPe,
            minDividendYield, maxDividendYield,
            minMarketCap, maxMarketCap,
            minPayoutRatio, maxPayoutRatio
        );

        PageResponse<TickerSummaryDTO> resp = new PageResponse<>(
            dtos.getContent(),
            dtos.getNumber(),
            dtos.getSize(),
            dtos.getTotalElements(),
            dtos.getTotalPages(),
            dtos.getNumberOfElements(),
            dtos.getSort()
        );

            return ResponseEntity.ok(resp);
        }
}
