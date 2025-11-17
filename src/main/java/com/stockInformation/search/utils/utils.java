package com.stockInformation.search.utils;

import org.springframework.web.util.HtmlUtils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Small collection of string helpers used by the search service.
 *
 * This class contains helpers used by the service layer to prepare user
 * queries for SQL pattern matching and to normalize company names before
 * inserting into the database for search/lookup purposes.
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

	// ---------------------------------------------------------------------
	// Company name normalization used when inserting/updating records.
	// Produces a compact lowercase string used for similarity/trigram
	// index fields (e.g. company_name_search).
	// ---------------------------------------------------------------------

	// Build the removal list (mirrors the project's python-style lists).
	private static final List<String> LEGAL_SUFFIXES = Arrays.asList(
			"incorporated", "corporation", "company", "limited",
			"inc.", "inc", "corp.", "corp", "co.", "co", "ltd.", "ltd",
			"llc", "l.l.c.", "l.l.c", "lp", "l.p.", "l.p", "llp", "l.l.p.", "l.l.p",
			"plc",
			"gmbh", "ag", "s.a.", "s.a", "sa", "n.v.", "n.v", "nv", "b.v.", "b.v", "bv",
			"spa", "s.p.a.", "s.p.a", "sarl", "s.a.r.l.", "s.a.r.l",
			"pty.", "pty", "srl", "s.r.l.", "s.r.l",
			"a/s", "as", "oy", "ab", "oyj"
	);

	private static final List<String> STRUCTURE_WORDS = Arrays.asList(
			"holdings", "holding", "hldgs", "hldg",
			"group", "group.",
			"partners", "partner",
			"trust", "trustee", "trustees",
			"fund", "funds", "investment", "investments", "capital", "ventures", "venture",
			"management", "mgmt", "advisors", "advisor", "advisory",
			"securities", "assets",
			"realty", "properties", "property",
			"bancorp", "financial", "finance"
	);

	private static final List<String> TRANSACTION_WORDS = Arrays.asList(
			"acquisition", "acquisitions", "acquire", "acquired", "acq.", "acq",
			"merger", "mergers", "merged", "mrg",
			"purchase", "purchases", "bought", "buying",
			"takeover", "spin-off", "spinoff",
			"divestiture", "divest", "divests",
			"reorganization", "reorg", "restructuring"
	);

	private static final List<String> INDUSTRY_WORDS = Arrays.asList(
			"technology", "technologies", "tech", "systems", "solutions", "software",
			"services", "service", "networks", "network", "telecom", "telecommunications", "communications",
			"international", "intl", "intl.", "global", "globals", "worldwide",
			"industrial", "industries", "industrials",
			"manufacturing", "manuf", "manuf.",
			"engineering", "engineering.",
			"digital", "media", "energy", "resources",
			"healthcare", "health", "medical", "bio", "biotech", "biosciences", "bioscience",
			"pharmaceutical", "pharma", "pharmaceuticals",
			"retail", "consumer", "commercial"
	);

	private static final List<String> STOPWORDS = Arrays.asList("the", "of", "for", "by", "in");

	private static final List<String> ABBREVIATIONS = Arrays.asList(
			"svc", "svc.", "svcs", "svcs.",
			"sys", "sys.",
			"mfg", "mfg.",
			"trx",
			"biz"
	);

	private static final List<String> FINANCIAL_TERMS = Arrays.asList("etf", "etn", "reit", "spv", "spac");

	private static final List<String> REMOVAL_WORDS = initRemovalWords();

	private static List<String> initRemovalWords() {
		List<String> combined = new ArrayList<>();
		combined.addAll(LEGAL_SUFFIXES);
		combined.addAll(STRUCTURE_WORDS);
		combined.addAll(TRANSACTION_WORDS);
		combined.addAll(INDUSTRY_WORDS);
		combined.addAll(STOPWORDS);
		combined.addAll(ABBREVIATIONS);
		combined.addAll(FINANCIAL_TERMS);

		// Deduplicate and sort by descending length so longer phrases match first
		List<String> uniq = combined.stream().map(String::toLowerCase).distinct().collect(Collectors.toList());
		uniq.sort((a, b) -> Integer.compare(b.length(), a.length()));
		return Collections.unmodifiableList(uniq);
	}

	/**
	 * Normalize company name for search index/storage.
	 * See the project's normalization rules (remove legal suffixes, punctuation,
	 * diacritics, HTML entities; return a compact lowercase string with no spaces).
	 *
	 * @param companyName original company name
	 * @return normalized string suitable for company_name_search columns
	 */
	public static String normalizeCompanyNameForSearch(String companyName) {
		if (companyName == null) return "";

		String normalized = Normalizer.normalize(companyName, Normalizer.Form.NFKC);
		normalized = normalized.toLowerCase(Locale.ROOT);

		// Decode common HTML entities (spring util provides this)
		String unescaped = HtmlUtils.htmlUnescape(normalized);
		if (unescaped != null) {
			normalized = unescaped;
		}
		
		// Decompose and remove diacritic marks
		String decomposed = Normalizer.normalize(normalized, Normalizer.Form.NFKD);
		normalized = decomposed.replaceAll("\\p{M}", "");		
		// Replace ampersand with space (so 'johnson & johnson' -> 'johnson johnson')
		normalized = normalized.replace("&", "");
			
		// Remove any remaining punctuation/symbols; keep letters, digits and spaces
		normalized = normalized.replaceAll("[^a-z0-9\\s]", "");
				
		// Remove common removal words as whole words
		if (!REMOVAL_WORDS.isEmpty()) {
			String pattern = REMOVAL_WORDS.stream()
					.map(java.util.regex.Pattern::quote)
					.collect(Collectors.joining("|"));
			if (!pattern.isEmpty()) {
				normalized = normalized.replaceAll("\\b(?:(" + pattern + "))\\b", " ");
			}
		}

		// Collapse whitespace and remove all spaces to produce the compact key
		normalized = normalized.replaceAll("\\s+", "");
		normalized = normalized.trim();

		return normalized;
	}
}

