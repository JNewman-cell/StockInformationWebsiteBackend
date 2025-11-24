-- ============================================================================
-- Migration: V4 - Create Ticker Summary Table
-- ============================================================================
-- Description: Creates ticker summary table with market metrics and ratios
-- Date: 2025-11-04
-- ============================================================================

CREATE TABLE IF NOT EXISTS ticker_summary (
    ticker VARCHAR(20) NOT NULL,
    cik INTEGER,
    market_cap BIGINT NOT NULL,
    previous_close NUMERIC(15,2) NOT NULL,
    pe_ratio NUMERIC(10,2),
    forward_pe_ratio NUMERIC(10,2),
    dividend_yield NUMERIC(5,2),
    payout_ratio NUMERIC(5,2),
    annual_dividend_growth NUMERIC(5,2),
    five_year_avg_dividend_yield NUMERIC(5,2),
    fifty_day_average NUMERIC(10,2) NOT NULL,
    two_hundred_day_average NUMERIC(10,2) NOT NULL,
    CONSTRAINT "idx_ticker_summary_ticker" PRIMARY KEY (ticker),
    CONSTRAINT "cik" FOREIGN KEY (cik) REFERENCES cik_lookup(cik)
);

-- Indexes: include partial indexes for nullable ratio/percentage columns and a lower-case index for case-insensitive search
-- Ensure pg_trgm extension is available for trigram GIN indexes (required for gin_trgm_ops)
CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX IF NOT EXISTS "idx_ticker_summary_dividend_yield" ON ticker_summary(dividend_yield) WHERE dividend_yield IS NOT NULL;
CREATE INDEX IF NOT EXISTS "idx_ticker_summary_forward_pe_ratio" ON ticker_summary(forward_pe_ratio) WHERE forward_pe_ratio IS NOT NULL;
CREATE INDEX IF NOT EXISTS "idx_ticker_summary_payout_ratio" ON ticker_summary(payout_ratio) WHERE payout_ratio IS NOT NULL;
CREATE INDEX IF NOT EXISTS "idx_ticker_summary_pe_ratio" ON ticker_summary(pe_ratio) WHERE pe_ratio IS NOT NULL;
CREATE INDEX IF NOT EXISTS "idx_ticker_summary_annual_dividend_growth" ON ticker_summary(annual_dividend_growth);
CREATE INDEX IF NOT EXISTS "idx_ticker_summary_market_cap" ON ticker_summary(market_cap);
CREATE INDEX IF NOT EXISTS "idx_ticker_summary_previous_close" ON ticker_summary(previous_close);
CREATE INDEX IF NOT EXISTS "idx_ticker_summary_ticker_lower" ON ticker_summary(lower(ticker::text));
-- Add a GIN trigram index to support fast case-insensitive trigram searches on ticker
CREATE INDEX IF NOT EXISTS "idx_ticker_summary_ticker_lower_trgm" ON ticker_summary USING gin (lower(ticker::text) gin_trgm_ops);

-- Enable row level security and create policy_1 as specified
ALTER TABLE ticker_summary ENABLE ROW LEVEL SECURITY;
CREATE POLICY "policy_1" ON ticker_summary USING (true);

-- Add table and column comments
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
COMMENT ON COLUMN ticker_summary.annual_dividend_growth IS 'Annual dividend growth rate as a percentage (0-999.99)';
COMMENT ON COLUMN ticker_summary.five_year_avg_dividend_yield IS 'Five-year average dividend yield as percentage (0-999.99)';
