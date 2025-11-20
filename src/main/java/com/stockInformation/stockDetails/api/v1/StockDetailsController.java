package com.stockInformation.stockDetails.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stockInformation.stockDetails.dto.SummaryResponse;

@RestController
@RequestMapping("/api/v1/stock-details")
@RequiredArgsConstructor
public class StockDetailsController {
    /**
     * Get detailed ticker summary by stock ticker
     */
    @GetMapping("/summary/{ticker}")
    public ResponseEntity<SummaryResponse> getCikLookupByCik(@PathVariable String ticker) {
        return null;
    }
}
