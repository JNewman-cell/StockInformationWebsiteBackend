package com.stockInformation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RootController {

    /**
     * Welcome endpoint for the root path
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> welcome() {
        return ResponseEntity.ok(Map.of(
            "message", "Welcome to Stock Information API",
            "version", "1.0.0",
            "documentation", "/swagger-ui/index.html",
            "endpoints", Map.of(
                "cik-lookup", "/api/v1/cik-lookup/{cik}",
                "ticker-summary", "/api/v1/ticker-summary",
                "search", "/api/v1/search/auto-complete"
            )
        ));
    }
}