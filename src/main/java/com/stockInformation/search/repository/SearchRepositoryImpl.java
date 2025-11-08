package com.stockInformation.search.repository;

import com.stockInformation.search.dto.AutocompleteResult;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SearchRepositoryImpl implements SearchRepository {

    private final EntityManager entityManager;

    public SearchRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<AutocompleteResult> searchByInputIgnoreCase(String query) {
        String sql = "SELECT t.ticker AS symbol, c.company_name AS name, "
            + "CAST(((CASE WHEN LOWER(t.ticker) = ?1 THEN 1 "
            + "            WHEN LOWER(t.ticker) LIKE CONCAT(?1, '%') THEN 0.95 "
            + "            ELSE similarity(LOWER(t.ticker), ?1) END) "
            + "       + (CASE WHEN LOWER(c.company_name) = ?1 THEN 1 "
            + "            WHEN LOWER(c.company_name) LIKE CONCAT(?1, '%') THEN 0.95 "
            + "            ELSE similarity(LOWER(c.company_name), ?1) END)) / 2.0 AS double precision) AS score "
            + "FROM ticker_summary t "
            + "JOIN cik_lookup c ON t.cik = c.cik "
            + "WHERE LOWER(t.ticker) LIKE CONCAT(?1, '%') "
            + "OR LOWER(c.company_name) LIKE CONCAT(?1, '%') "
            + "OR similarity(LOWER(t.ticker), ?1) > 0.2 "
            + "OR similarity(LOWER(c.company_name), ?1) > 0.2 "
            + "ORDER BY score DESC "
            + "LIMIT 10";

        Query nativeQuery = entityManager.createNativeQuery(sql);
        nativeQuery.setParameter(1, query);

        @SuppressWarnings("unchecked")
        List<Object[]> results = nativeQuery.getResultList();

        return results.stream()
            .map(row -> new AutocompleteResult((String) row[0], (String) row[1], (Double) row[2]))
            .collect(Collectors.toList());
    }
}