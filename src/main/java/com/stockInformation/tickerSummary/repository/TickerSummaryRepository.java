package com.stockInformation.tickerSummary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.stockInformation.tickerSummary.entity.TickerSummary;

import java.util.Optional;

@Repository
public interface TickerSummaryRepository extends JpaRepository<TickerSummary, String>, JpaSpecificationExecutor<TickerSummary>, TickerSummaryCompanyRepository {

    /**
     * Find ticker summary by ticker symbol (case-insensitive)
     */
    @EntityGraph(attributePaths = {"cikLookup"})
    @Query("SELECT t FROM TickerSummary t WHERE LOWER(t.ticker) = :ticker")
    Optional<TickerSummary> findByTickerIgnoreCase(@Param("ticker") String ticker); 
}
