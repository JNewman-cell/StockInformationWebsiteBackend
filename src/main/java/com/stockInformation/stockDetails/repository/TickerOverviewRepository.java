package com.stockInformation.stockDetails.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stockInformation.stockDetails.entity.TickerOverview;

import java.util.Optional;

@Repository
public interface TickerOverviewRepository extends JpaRepository<TickerOverview, String> {

    /**
     * Find ticker overview by ticker symbol (case-insensitive)
     */
    Optional<TickerOverview> findByTickerIgnoreCase(String ticker);

}
