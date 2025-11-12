package com.stockInformation.tickerSummary.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stockInformation.tickerSummary.dto.TickerSummaryDTO;
import com.stockInformation.tickerSummary.service.TickerSummaryService;
import com.stockInformation.common.dto.PageResponse;

import java.math.BigDecimal;
import java.util.Optional;

import com.stockInformation.tickerSummary.utils.TickerSummaryValidationUtils;

@RestController
@RequestMapping("/api/v1/ticker-summary")
@RequiredArgsConstructor
public class TickerSummaryController {

    private final TickerSummaryService tickerSummaryService;
    
    /**
     * Get ticker summary by ticker symbol
     */
    @GetMapping("/{ticker}")
    public ResponseEntity<TickerSummaryDTO> getTickerSummaryByTicker(@PathVariable String ticker) {
        Optional<TickerSummaryDTO> tickerSummaryDTO = tickerSummaryService.findDTOByTicker(ticker);
        return tickerSummaryDTO
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
        if (!TickerSummaryValidationUtils.isValidPage(page)) {
            return ResponseEntity.badRequest().build();
        }
        if (!TickerSummaryValidationUtils.isValidPageSize(pageSize)) {
            return ResponseEntity.badRequest().build();
        }

        if (!TickerSummaryValidationUtils.isValidSortOrder(sortOrder)) {
            return ResponseEntity.badRequest().build();
        }
        if (!TickerSummaryValidationUtils.isValidSortBy(sortBy)) {
            return ResponseEntity.badRequest().build();
        }

        if (!TickerSummaryValidationUtils.isValidMarketCap(minMarketCap)) {
            return ResponseEntity.badRequest().build();
        }
        if (!TickerSummaryValidationUtils.isValidMarketCap(maxMarketCap)) {
            return ResponseEntity.badRequest().build();
        }

        if (!TickerSummaryValidationUtils.isValidPreviousClose(minPreviousClose)) {
            return ResponseEntity.badRequest().build();
        }
        if (!TickerSummaryValidationUtils.isValidPreviousClose(maxPreviousClose)) {
            return ResponseEntity.badRequest().build();
        }

        if (!TickerSummaryValidationUtils.isValidPercentage(minDividendYield)) {
            return ResponseEntity.badRequest().build();
        }
        if (!TickerSummaryValidationUtils.isValidPercentage(maxDividendYield)) {
            return ResponseEntity.badRequest().build();
        }

        if (!TickerSummaryValidationUtils.isValidPercentage(minPayoutRatio)) {
            return ResponseEntity.badRequest().build();
        }
        if (!TickerSummaryValidationUtils.isValidPercentage(maxPayoutRatio)) {
            return ResponseEntity.badRequest().build();
        }

        // PE ratios can be negative (no validation needed)

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
