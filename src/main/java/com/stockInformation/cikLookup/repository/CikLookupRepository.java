package com.stockInformation.cikLookup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.stockInformation.cikLookup.entity.CikLookup;

import java.util.Optional;

@Repository
public interface CikLookupRepository extends JpaRepository<CikLookup, Integer> {

    /**
     * Find CIK lookup by CIK number
     */
    @Query("SELECT c FROM CikLookup c WHERE c.cik = :cik")
    Optional<CikLookup> findByCik(@Param("cik") Integer cik);
}