package com.stockInformation.tickerSummary.utils;

import java.math.BigDecimal;
import java.util.Set;

public class TickerSummaryValidationUtils {

    public static final Set<Integer> PAGE_SIZES = Set.of(5, 10, 25, 50);

    public static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
        "ticker", "company_name", "previous_close",
        "pe", "forward_pe", "dividend_yield",
        "market_cap", "payout_ratio"
    );

    public static final Set<String> SORT_DIRECTIONS = Set.of(
        "ASC", "DESC"
    );

    public static final BigDecimal MAX_PERCENTAGE = new BigDecimal("999.99");

    public static boolean isValidPage(int page) {
        return page >= 0;
    }

    public static boolean isValidPageSize(int pageSize) {
        return PAGE_SIZES.contains(pageSize);
    }

    public static boolean isValidSortOrder(String sortOrder) {
        return SORT_DIRECTIONS.contains(sortOrder);
    }

    public static boolean isValidSortBy(String sortBy) {
        return ALLOWED_SORT_FIELDS.contains(sortBy);
    }

    public static boolean isValidMarketCap(Long marketCap) {
        return marketCap == null || marketCap > 0;
    }

    public static boolean isValidPreviousClose(BigDecimal previousClose) {
        return previousClose == null || previousClose.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isValidPercentage(BigDecimal percentage) {
        return percentage == null || (percentage.compareTo(BigDecimal.ZERO) >= 0 && percentage.compareTo(MAX_PERCENTAGE) <= 0);
    }

}