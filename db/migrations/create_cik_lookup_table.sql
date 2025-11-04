-- ============================================================================
-- Migration: V3 - Create CIK Lookup Table
-- ============================================================================
-- Description: Creates CIK lookup table for SEC company identification
-- Date: 2025-11-04
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

-- Add table and column comments
COMMENT ON TABLE cik_lookup IS 'Lookup table for CIK (Central Index Key) to company name mappings from SEC';
COMMENT ON COLUMN cik_lookup.cik IS 'Central Index Key - unique identifier assigned by SEC';
COMMENT ON COLUMN cik_lookup.company_name IS 'Official company name registered with SEC';
