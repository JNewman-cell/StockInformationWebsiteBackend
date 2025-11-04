-- ============================================================================
-- Stock Information Database Schema
-- ============================================================================
-- Description: Complete database schema for stock information management
-- Database: PostgreSQL 15+
-- Created: 2025-11-04
-- ============================================================================

-- ============================================================================
-- CIK Lookup Table
-- ============================================================================
CREATE TABLE IF NOT EXISTS cik_lookup (
    cik INTEGER PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT cik_lookup_pkey PRIMARY KEY (cik),
    CONSTRAINT cik_lookup_cik_positive CHECK (cik > 0)
);

-- Create index for company name lookups
CREATE INDEX IF NOT EXISTS idx_cik_lookup_company_name ON cik_lookup(company_name);

COMMENT ON TABLE cik_lookup IS 'Lookup table for CIK (Central Index Key) to company name mappings from SEC';
COMMENT ON COLUMN cik_lookup.cik IS 'Central Index Key - unique identifier assigned by SEC';
COMMENT ON COLUMN cik_lookup.company_name IS 'Official company name registered with SEC';

-- ============================================================================
-- Ticker Summary Table
-- ============================================================================
CREATE TABLE IF NOT EXISTS ticker_summary (
    ticker VARCHAR(20) PRIMARY KEY,
    cik INTEGER,
    market_cap BIGINT,
    previous_close NUMERIC(15,2),
    pe_ratio NUMERIC(10,2),
    forward_pe_ratio NUMERIC(10,2),
    dividend_yield NUMERIC(4,2),
    payout_ratio NUMERIC(4,2),
    fifty_day_average NUMERIC(10,2),
    two_hundred_day_average NUMERIC(10,2),
    CONSTRAINT ticker_summary_pkey PRIMARY KEY (ticker),
    CONSTRAINT ticker_summary_cik_fkey FOREIGN KEY (cik) REFERENCES cik_lookup(cik) ON DELETE SET NULL,
    CONSTRAINT ticker_summary_market_cap_positive CHECK (market_cap >= 0),
    CONSTRAINT ticker_summary_dividend_yield_range CHECK (dividend_yield >= 0 AND dividend_yield <= 99.99),
    CONSTRAINT ticker_summary_payout_ratio_range CHECK (payout_ratio >= 0 AND payout_ratio <= 99.99)
);

-- Create indexes for performance optimization
CREATE INDEX IF NOT EXISTS idx_ticker_summary_cik ON ticker_summary(cik);
CREATE INDEX IF NOT EXISTS idx_ticker_summary_market_cap ON ticker_summary(market_cap);
CREATE INDEX IF NOT EXISTS idx_ticker_summary_pe_ratio ON ticker_summary(pe_ratio);

COMMENT ON TABLE ticker_summary IS 'Comprehensive stock ticker summary data including market metrics and financial ratios';
COMMENT ON COLUMN ticker_summary.ticker IS 'Stock ticker symbol (primary key)';
COMMENT ON COLUMN ticker_summary.cik IS 'Foreign key reference to cik_lookup table';
COMMENT ON COLUMN ticker_summary.market_cap IS 'Market capitalization in dollars';
COMMENT ON COLUMN ticker_summary.pe_ratio IS 'Price-to-earnings ratio (trailing)';
COMMENT ON COLUMN ticker_summary.forward_pe_ratio IS 'Forward price-to-earnings ratio';
COMMENT ON COLUMN ticker_summary.dividend_yield IS 'Dividend yield as percentage (0-99.99)';
COMMENT ON COLUMN ticker_summary.payout_ratio IS 'Dividend payout ratio as percentage (0-99.99)';
COMMENT ON COLUMN ticker_summary.fifty_day_average IS '50-day moving average price';
COMMENT ON COLUMN ticker_summary.two_hundred_day_average IS '200-day moving average price';

-- ============================================================================
-- End of Schema
-- ============================================================================
