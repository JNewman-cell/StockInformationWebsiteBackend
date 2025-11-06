package com.stockInformation.tickerSummary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.stockInformation.tickerSummary.entity.TickerSummary;

import java.util.Optional;

@Repository
public interface TickerSummaryRepository extends JpaRepository<TickerSummary, String> {

    /**
     * Find ticker summary by ticker symbol (case-insensitive)
     */
    @Query("SELECT t FROM TickerSummary t WHERE UPPER(t.ticker) = UPPER(:ticker)")
    Optional<TickerSummary> findByTickerIgnoreCase(@Param("ticker") String ticker);

}
