package com.stockinfo.backend.api.v1.cik;

import com.stockinfo.backend.entity.CikLookup;
import com.stockinfo.backend.service.CikLookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cik-lookup")
@RequiredArgsConstructor
public class CikLookupController {

    private final CikLookupService cikLookupService;
    private final CikLookupMapper cikLookupMapper;

    /**
     * Get CIK lookup by CIK number
     */
    @GetMapping("/{cik}")
    public ResponseEntity<CikLookupDTO> getCikLookupByCik(@PathVariable Integer cik) {
        Optional<CikLookup> cikLookup = cikLookupService.findByCik(cik);
        return cikLookup
                .map(cikLookupMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
