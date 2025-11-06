package com.stockInformation.search.repository;

import com.stockInformation.search.dto.AutocompleteResult;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SearchRepository{

    @Query(value = "SELECT t.ticker AS symbol, c.company_name AS name, "
        + "CAST(((CASE WHEN LOWER(t.ticker) = :query THEN 1 "
        + "            WHEN LOWER(t.ticker) LIKE CONCAT(:query, '%') THEN 0.95 "
        + "            ELSE similarity(LOWER(t.ticker), :query) END) "
        + "       + (CASE WHEN LOWER(c.company_name) = :query THEN 1 "
        + "            WHEN LOWER(c.company_name) LIKE CONCAT(:query, '%') THEN 0.95 "
        + "            ELSE similarity(LOWER(c.company_name), :query) END)) / 2.0 AS double precision) AS score "
        + "FROM ticker_summary t "
        + "JOIN cik_lookup c ON t.cik = c.cik "
        + "WHERE LOWER(t.ticker) LIKE CONCAT(:query, '%') "
        + "OR LOWER(c.company_name) LIKE CONCAT(:query, '%') "
        + "OR similarity(LOWER(t.ticker), :query) > 0.2 "
        + "OR similarity(LOWER(c.company_name), :query) > 0.2 "
        + "ORDER BY score DESC "
        + "LIMIT 10",
        nativeQuery = true)
    List<AutocompleteResult> searchByInputIgnoreCase(@Param("query") String query);
}