package com.stockInformation.cikLookup.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stockInformation.cikLookup.entity.CikLookup;
import com.stockInformation.cikLookup.repository.CikLookupRepository;

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