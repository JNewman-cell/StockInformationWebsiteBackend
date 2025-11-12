package com.stockInformation.tickerSummary.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.stockInformation.cikLookup.entity.QCikLookup;
import com.stockInformation.tickerSummary.dto.TickerSummaryDTO;
import com.stockInformation.tickerSummary.entity.QTickerSummary;
import com.stockInformation.tickerSummary.transformer.SortOrderTransformer;

@Repository
public class TickerSummaryCompanyRepositoryImpl implements TickerSummaryCompanyRepository {

    private final JPAQueryFactory queryFactory;

	public TickerSummaryCompanyRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public Page<TickerSummaryDTO> findAllWithCompanyName(Predicate predicate, Pageable pageable) {
        QTickerSummary t = QTickerSummary.tickerSummary;
        QCikLookup c = QCikLookup.cikLookup;

        // Convert Spring Data Sort to Querydsl OrderSpecifiers
        OrderSpecifier<?>[] orderSpecifiers = pageable.getSort().stream()
            .map(order -> SortOrderTransformer.convertToOrderSpecifier(t, c, order))
            .toArray(OrderSpecifier[]::new);

        // Execute data query with DTO projection
        List<TickerSummaryDTO> content = queryFactory
            .select(Projections.constructor(
                TickerSummaryDTO.class,
                t.ticker,
                c.companyName,
                t.marketCap,
                t.previousClose,
                t.peRatio,
                t.forwardPeRatio,
                t.dividendYield,
                t.payoutRatio,
                t.fiftyDayAverage,
                t.twoHundredDayAverage
            ))
            .from(t)
            .leftJoin(t.cikLookup, c)
            .where(predicate)
            .orderBy(orderSpecifiers)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        // Execute optimized count query
        Long total = queryFactory
            .select(t.count())
            .from(t)
            .where(predicate)
            .fetchOne();

        long totalCount = (total != null) ? total : 0L;

        @SuppressWarnings("null")
        Page<TickerSummaryDTO> page = new PageImpl<>(content, pageable, totalCount);
        return page;
	}

    @Override
    public Optional<TickerSummaryDTO> findByTickerWithCompanyName(String ticker) {
        QTickerSummary t = QTickerSummary.tickerSummary;
        QCikLookup c = QCikLookup.cikLookup;

        TickerSummaryDTO result = queryFactory
            .select(Projections.constructor(
                TickerSummaryDTO.class,
                t.ticker,
                c.companyName,
                t.marketCap,
                t.previousClose,
                t.peRatio,
                t.forwardPeRatio,
                t.dividendYield,
                t.payoutRatio,
                t.fiftyDayAverage,
                t.twoHundredDayAverage
            ))
            .from(t)
            .leftJoin(t.cikLookup, c)
            .where(t.ticker.equalsIgnoreCase(ticker))
            .fetchOne();

        return Optional.ofNullable(result);
    }
}
