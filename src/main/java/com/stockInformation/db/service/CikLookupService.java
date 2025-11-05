package com.stockInformation.db.service;

import com.stockInformation.db.entity.CikLookup;
import com.stockInformation.db.repository.CikLookupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CikLookupService {

    private final CikLookupRepository cikLookupRepository;

    /**
     * Find CIK lookup by CIK number
     */
    @Transactional(readOnly = true)
    public Optional<CikLookup> findByCik(Integer cik) {
        return cikLookupRepository.findByCik(cik);
    }
}