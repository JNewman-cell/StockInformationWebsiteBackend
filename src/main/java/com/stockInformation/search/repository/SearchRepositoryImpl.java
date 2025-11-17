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
        String sql = """
                WITH ticker_hits AS (
                    SELECT
                        t.cik,
                        t.ticker,
                        c.company_name,
                        similarity(LOWER(t.ticker), LOWER(?1)) AS s_ticker
                    FROM ticker_summary t
                    JOIN cik_lookup c ON t.cik = c.cik
                    WHERE LOWER(t.ticker) % LOWER(?1)
                ),

                company_hits AS (
                    SELECT
                        t.cik,
                        t.ticker,
                        c.company_name,
                        similarity(company_name_search, LOWER(?1)) AS s_company
                    FROM cik_lookup c
                    JOIN ticker_summary t ON t.cik = c.cik
                    WHERE c.company_name_search % LOWER(?1)
                )

                SELECT
                    COALESCE(t.ticker, c.ticker) AS ticker,
                    COALESCE(t.company_name, c.company_name) AS company_name,
                    CAST((COALESCE(t.s_ticker, 0) + COALESCE(c.s_company, 0)) / 2.0 AS double precision) AS score
                FROM ticker_hits t
                FULL OUTER JOIN company_hits c ON t.cik = c.cik
                ORDER BY score DESC
                LIMIT 10
                """;


        Query nativeQuery = entityManager.createNativeQuery(sql);
        // set positional parameter ?1
        nativeQuery.setParameter(1, query);

        @SuppressWarnings("unchecked")
        List<Object[]> results = nativeQuery.getResultList();

        return results.stream()
            .map(row -> {
                String ticker = (String) row[0];
                String companyName = (String) row[1];
                Double score = row[2] != null ? ((Number) row[2]).doubleValue() : null;
                return new AutocompleteResult(ticker, companyName, score);
            })
            .collect(Collectors.toList());
    }
}