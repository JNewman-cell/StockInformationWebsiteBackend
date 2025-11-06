package com.stockInformation.cikLookup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stockInformation.cikLookup.entity.CikLookup;

import java.util.Optional;

@Repository
public interface CikLookupRepository extends JpaRepository<CikLookup, Integer> {

    /**
     * Find CIK lookup by CIK number
     */
    Optional<CikLookup> findByCik(Integer cik);
}