package com.stockInformation.db.repository;

import com.stockInformation.db.entity.CikLookup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CikLookupRepository extends JpaRepository<CikLookup, Integer> {

    /**
     * Find CIK lookup by CIK number
     */
    Optional<CikLookup> findByCik(Integer cik);
}