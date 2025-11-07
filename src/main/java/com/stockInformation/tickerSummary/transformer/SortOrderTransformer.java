package com.stockInformation.tickerSummary.transformer;

import org.springframework.data.domain.Sort;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.stockInformation.cikLookup.entity.QCikLookup;
import com.stockInformation.tickerSummary.entity.QTickerSummary;

/**
 * Utility to convert Spring Data Sort.Order to Querydsl OrderSpecifier
 */
public final class SortOrderTransformer {

    private SortOrderTransformer() {}

    public static OrderSpecifier<?> convertToOrderSpecifier(QTickerSummary t, QCikLookup c, Sort.Order order) {
        Order direction = order.isAscending() ? Order.ASC : Order.DESC;

        return switch (order.getProperty().toLowerCase()) {
            case "companyname", "company_name" -> new OrderSpecifier<>(direction, c.companyName);
            case "previousclose", "previous_close" -> new OrderSpecifier<>(direction, t.previousClose);
            case "peratio", "pe", "pe_ratio" -> new OrderSpecifier<>(direction, t.peRatio);
            case "forwardperatio", "forward_pe", "forward_pe_ratio" -> new OrderSpecifier<>(direction, t.forwardPeRatio);
            case "dividendyield", "dividend_yield" -> new OrderSpecifier<>(direction, t.dividendYield);
            case "marketcap", "market_cap" -> new OrderSpecifier<>(direction, t.marketCap);
            case "payoutratio", "payout_ratio" -> new OrderSpecifier<>(direction, t.payoutRatio);
            case "fiftydayaverage", "fifty_day_average" -> new OrderSpecifier<>(direction, t.fiftyDayAverage);
            case "twohundreddayaverage", "two_hundred_day_average" -> new OrderSpecifier<>(direction, t.twoHundredDayAverage);
            default -> new OrderSpecifier<>(direction, t.ticker); // default to ticker
        };
    }
}
