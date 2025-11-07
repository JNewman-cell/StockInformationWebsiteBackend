package com.stockInformation.tickerSummary.utils;

import com.querydsl.core.types.OrderSpecifier;

/**
 * Utility helpers for ticker summary module
 */
public final class utils {

	/**
	 * Apply nulls ordering to the provided OrderSpecifier based on ascending flag.
	 * If ascending is true, returns os.nullsFirst(), otherwise os.nullsLast().
	 */
	public static OrderSpecifier<?> applyNullHandling(OrderSpecifier<?> os, boolean ascending) {
		return ascending ? os.nullsFirst() : os.nullsLast();
	}
}
