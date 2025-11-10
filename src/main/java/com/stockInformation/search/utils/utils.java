package com.stockInformation.search.utils;

/**
 * Small collection of string helpers used by the search service.
 *
 * This class contains a helper used by the service layer to prepare user
 * queries for SQL pattern matching. It replaces special characters with
 * '%' so the native LIKE/similarity queries can match across those
 * characters.
 */
public final class utils {

	private utils() {
		// utility
	}

	/**
	 * Sanitize a query string for SQL pattern matching.
	 *
	 * Behavior:
	 *  - If the query contains at least one alphanumeric character (a-z, 0-9)
	 *    we replace any run of non-alphanumeric characters with a single '%'.
	 *    This turns characters like spaces, dashes, slashes, punctuation into
	 *    SQL wildcards so the LIKE/similarity queries can match across them.
	 *  - If the query contains no alphanumeric characters we return the
	 *    original query unchanged (so callers that expect literal chars still
	 *    receive them).
	 *
	 * Note: callers are expected to lowercase the input if they want
	 * case-insensitive matching (the repository uses LOWER(...)).
	 *
	 * @param query original (already-normalized) query
	 * @return sanitized query suitable to pass as the SQL parameter
	 */
	public static String sanitizeQueryForSql(String query) {
		if (query == null) return null;

		// If there are no alphanumeric characters, don't convert everything to '%'
		// (that would turn the query into a global wildcard). Return the
		// original so repository logic can treat it literally and avoid
		// accidental full-table matches.
		if (!query.matches(".*[a-z0-9].*")) {
			return query;
		}

		// Replace any run of non-alphanumeric characters with a single '%'
		String replaced = query.replaceAll("[^a-z0-9]+", "%");

		// Collapse multiple consecutive '%' into one (defensive)
		replaced = replaced.replaceAll("%+", "%");

		return replaced;
	}
}

