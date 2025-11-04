# Database Schema Documentation

This directory contains the database schema definitions and migrations for the Stock Information Backend application.

## Directory Structure

```
db/
├── README.md                           # This file
├── schema.sql                          # Complete database schema
└── migrations/                         # Version-controlled schema migrations
    ├── V1__initial_stocks_table.sql
    ├── V2__add_exchange_column.sql
    ├── V3__create_cik_lookup_table.sql
    └── V4__create_ticker_summary_table.sql
```

## Database Schema

The application uses PostgreSQL with three main tables:

### 1. stocks
Core table storing basic stock information and pricing data.

**Columns:**
- `id` - Auto-incrementing primary key
- `symbol` - Unique stock ticker symbol
- `company_name` - Full company name
- `current_price` - Current stock price
- `previous_close` - Previous closing price
- `day_high` - Day's high price
- `day_low` - Day's low price
- `volume` - Trading volume
- `sector` - Business sector
- `exchange` - Stock exchange (NYSE, NASDAQ, etc.)
- `created_at` - Record creation timestamp
- `updated_at` - Record update timestamp

### 2. cik_lookup
Lookup table for SEC Central Index Key (CIK) to company name mappings.

**Columns:**
- `cik` - Central Index Key (primary key)
- `company_name` - Official SEC registered company name
- `created_at` - Record creation timestamp
- `last_updated_at` - Record update timestamp

### 3. ticker_summary
Comprehensive stock ticker summary with market metrics and financial ratios.

**Columns:**
- `ticker` - Stock ticker symbol (primary key)
- `cik` - Foreign key to cik_lookup
- `market_cap` - Market capitalization
- `previous_close` - Previous closing price
- `pe_ratio` - Price-to-earnings ratio (trailing)
- `forward_pe_ratio` - Forward P/E ratio
- `dividend_yield` - Dividend yield percentage (0-99.99)
- `payout_ratio` - Dividend payout ratio percentage (0-99.99)
- `fifty_day_average` - 50-day moving average
- `two_hundred_day_average` - 200-day moving average

## Migrations

The `migrations/` directory contains versioned SQL migration files following the naming convention:

```
V{version}__{description}.sql
```

### Migration Order

1. **V1** - Creates initial stocks table
2. **V2** - Adds exchange column to stocks
3. **V3** - Creates cik_lookup table
4. **V4** - Creates ticker_summary table with foreign key to cik_lookup

## Applying Schema Changes

### Full Schema Setup (Fresh Database)

To create all tables from scratch:

```bash
psql <your-database-url> -f db/schema.sql
```

### Incremental Migrations

To apply migrations one by one:

```bash
# Apply specific migration
psql <your-database-url> -f db/migrations/V1__initial_stocks_table.sql
psql <your-database-url> -f db/migrations/V2__add_exchange_column.sql
psql <your-database-url> -f db/migrations/V3__create_cik_lookup_table.sql
psql <your-database-url> -f db/migrations/V4__create_ticker_summary_table.sql
```

### Using Connection String from application.properties

```bash
# Extract connection details from application.properties
DB_URL="jdbc:postgresql://your-host/your-database?sslmode=require"

# Apply schema
psql "postgresql://username:password@host/database?sslmode=require" -f db/schema.sql
```

## Best Practices

1. **Never modify existing migrations** - Once a migration is applied, create a new migration for changes
2. **Test migrations on development database first** - Always test before applying to production
3. **Use transactions** - Migrations use implicit transactions; ensure atomic changes
4. **Document changes** - Include comments in migration files explaining the purpose
5. **Backup before migrating** - Always backup production database before applying migrations

## JPA/Hibernate Configuration

The application uses JPA with Hibernate. The `spring.jpa.hibernate.ddl-auto=update` setting in `application.properties` allows Hibernate to automatically create/update tables based on entity classes.

However, for production environments, it's recommended to:
- Set `spring.jpa.hibernate.ddl-auto=validate` 
- Manage schema changes explicitly through SQL migrations

## Indexes

The schema includes indexes on frequently queried columns for performance:

- **stocks**: symbol, sector, exchange, company_name
- **cik_lookup**: company_name
- **ticker_summary**: cik, market_cap, pe_ratio

## Constraints

The schema enforces data integrity through:

- **Unique constraints**: Stock symbols must be unique
- **Foreign keys**: ticker_summary.cik references cik_lookup.cik
- **Check constraints**: Positive values for prices and valid ranges for ratios
- **NOT NULL constraints**: Required fields cannot be null

## Data Types

- **BIGSERIAL**: Auto-incrementing 64-bit integers for IDs
- **VARCHAR(n)**: Variable-length character strings with max length
- **NUMERIC(p,s)**: Exact decimal numbers with precision and scale
- **BIGINT**: 64-bit integers for large numbers (market cap, volume)
- **TIMESTAMP**: Date and time values

## Related Resources

- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [Hibernate ORM Documentation](https://hibernate.org/orm/documentation/)
