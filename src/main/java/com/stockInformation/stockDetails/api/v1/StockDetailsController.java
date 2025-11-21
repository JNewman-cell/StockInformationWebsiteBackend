package com.stockInformation.stockDetails.api.v1;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stockInformation.stockDetails.dto.DetailsSummaryResponse;
import com.stockInformation.stockDetails.service.StockDetailsService;

@RestController
@RequestMapping("/api/v1/stock-details")
@RequiredArgsConstructor
public class StockDetailsController {
    private final StockDetailsService stockDetailsService;

    /**
     * Get detailed ticker summary by stock ticker
     */
    @GetMapping("/summary/{ticker}")
    public ResponseEntity<DetailsSummaryResponse> getStockDetailsSummaryByTicker(@PathVariable String ticker) {
        if (ticker == null || ticker.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<DetailsSummaryResponse> responseOpt = stockDetailsService.getStockDetailsSummaryByTicker(ticker);
        
        if (responseOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(responseOpt.get());
    }
}
