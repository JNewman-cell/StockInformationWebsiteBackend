-- ============================================================================
-- Migration: V3 - Create CIK Lookup Table
-- ============================================================================
-- Description: Creates CIK lookup table for SEC company identification
-- Date: 2025-11-04
-- ============================================================================

CREATE TABLE IF NOT EXISTS cik_lookup (
    cik INTEGER PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL DEFAULT 'Company',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT cik_lookup_cik_positive CHECK (cik > 0)
);

-- Create index for company name lookups
-- Ensure pg_trgm extension is available for trigram GIN indexes (required for gin_trgm_ops)
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Case-insensitive btree index for equality/ordering on lower(company_name)
CREATE INDEX IF NOT EXISTS "idx_cik_lookup_company_name_lower" ON cik_lookup(lower(company_name::text));

-- GIN trigram index to support fast similarity / ILIKE '%...%' searches on company_name
CREATE INDEX IF NOT EXISTS "idx_cik_lookup_company_name_lower_trgm" ON cik_lookup USING gin (lower(company_name::text) gin_trgm_ops);

-- Add table and column comments
COMMENT ON TABLE cik_lookup IS 'Lookup table for CIK (Central Index Key) to company name mappings from SEC';
COMMENT ON COLUMN cik_lookup.cik IS 'Central Index Key - unique identifier assigned by SEC';
COMMENT ON COLUMN cik_lookup.company_name IS 'Official company name registered with SEC';
