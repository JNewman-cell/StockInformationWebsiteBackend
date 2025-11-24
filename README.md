# Stock Information Website Backend

A high-performance Java backend application engineered for rapid stock data retrieval and analysis. Built with Spring Boot 3.5.7 and Java 25, this service leverages advanced database optimization techniques and modern caching strategies to deliver sub-second response times for complex financial queries.

## Architecture Overview

This application implements a **layered architecture** with a strong focus on **query optimization**, **database indexing**, and **distributed caching** to handle high-throughput financial data requests efficiently.

### Core Design Principles

1. **Query Performance First**: Every database interaction is optimized using DTO projections, strategic indexing, and Querydsl
2. **Zero N+1 Queries**: Leverages JPA fetch strategies and optimized joins to eliminate redundant database round trips
3. **Cache-Aside Pattern**: Redis-backed caching with TTL management reduces database load for frequently accessed data
4. **Type-Safe Queries**: Compile-time query validation via Querydsl prevents runtime SQL errors
5. **Stateless REST Design**: Horizontally scalable architecture with no session affinity requirements

## Performance Engineering Highlights

### 1. Advanced Database Optimization

#### PostgreSQL Trigram Indexing (pg_trgm)
- **GIN trigram indexes** enable lightning-fast fuzzy search across ticker symbols and company names
- Supports efficient `LIKE '%pattern%'` queries and similarity scoring without full table scans
- Example: `CREATE INDEX idx_ticker_lower_trgm ON ticker_summary USING gin (lower(ticker) gin_trgm_ops)`

#### Strategic Index Design
- **Partial indexes** on nullable columns (PE ratios, dividend yields) reduce index size and improve write performance
- **Composite indexes** for common filter combinations (market cap + sector queries)
- **B-tree indexes** for range queries and sorting operations
- **Case-insensitive indexes** using `lower()` function for efficient text searches

#### Query Optimization with Querydsl
- **DTO projections** at the database layer - only fetch required columns, not entire entities
- **Dynamic predicate composition** allows complex filtering without N+1 queries
- **Optimized count queries** separate from data queries to avoid unnecessary joins

```java
// Efficient DTO projection - fetches only needed columns
queryFactory
    .select(Projections.constructor(TickerSummaryDTO.class,
        t.ticker, c.companyName, t.marketCap, /* specific fields */))
    .from(t)
    .leftJoin(t.cikLookup, c)  // Single join, no N+1
    .where(predicate)
    .fetch();
```

### 2. Distributed Caching with Redis

#### Multi-Tier Caching Strategy
- **L1 Cache**: JPA second-level cache for entity-level caching
- **L2 Cache**: Redis-backed distributed cache for DTO responses
- **Cache-Control Headers**: Client-side caching for static financial data

#### Cache Configuration
- **TTL Management**: Different expiration policies per data type
  - Stock details: 15 minutes (less volatile)
  - Autocomplete results: 15 minutes (frequent updates)
- **Cache Warming**: Preload frequently accessed tickers on startup
- **Lettuce Client**: Non-blocking Redis client with connection pooling

```java
@Cacheable(value = "stockdetailssummary", key = "#ticker.toLowerCase()")
public Optional<DetailsSummaryResponse> getStockDetails(String ticker) {
    // Cache miss: fetch from database and cache result
    // Cache hit: return from Redis instantly
}
```

### 3. Search Optimization with Similarity Scoring

#### Fuzzy Search Algorithm
- **PostgreSQL similarity() function** with pg_trgm for ranked search results
- **FULL OUTER JOIN** strategy combines ticker and company name matches
- **Composite scoring**: Averages ticker similarity and company name similarity
- **LIMIT 10**: Returns only top results to minimize network transfer

```sql
-- Combines ticker and company name hits with weighted scoring
SELECT 
    COALESCE(t.ticker, c.ticker) AS ticker,
    CAST((COALESCE(t.s_ticker, 0) + COALESCE(c.s_company, 0)) / 2.0 AS double precision) AS score
FROM ticker_hits t
FULL OUTER JOIN company_hits c ON t.cik = c.cik
ORDER BY score DESC
LIMIT 10
```

### 4. Security & Authentication

#### OAuth 2.0 Resource Server
- **JWT validation** with Neon Auth integration
- **Custom bearer token resolver**: Supports both `x-stack-access-token` header and standard `Authorization` header
- **Stateless authentication**: No session storage, enabling horizontal scaling
- **Public endpoints**: Search and ticker lists accessible without authentication for broader accessibility

### 5. API Design & Validation

#### RESTful Best Practices
- **Versioned APIs** (`/api/v1/*`) for backward compatibility
- **Spring Validation**: Bean validation at controller layer prevents invalid data from reaching services
- **Global exception handling**: Consistent error responses across all endpoints
- **Pagination support**: Offset-based pagination for large result sets

## Technology Stack

### Core Framework
- **Java 25**: Latest LTS with enhanced virtual threads and performance improvements
- **Spring Boot 3.5.7**: Modern Spring framework with native compilation support
- **Spring Data JPA**: Repository abstraction with Hibernate 6.x

### Database Layer
- **PostgreSQL**: Production database with advanced indexing capabilities
- **pg_trgm Extension**: Trigram-based fuzzy searching
- **Querydsl 5.1.0**: Type-safe dynamic query builder with JPA integration
- **Flyway/SQL Migrations**: Version-controlled database schema management

### Performance & Caching
- **Redis**: Distributed cache with Lettuce client
- **Spring Cache Abstraction**: Declarative caching with annotation-based configuration
- **Connection Pooling**: HikariCP for optimized database connection management

### Code Quality & Mapping
- **Lombok**: Reduces boilerplate with compile-time code generation
- **MapStruct 1.6.3**: Compile-time bean mapping (zero reflection overhead)
- **Custom Transformers**: Hand-optimized object transformations for critical paths

### Testing & Documentation
- **TestContainers**: Integration tests with real PostgreSQL instances
- **H2 Database**: In-memory database for fast unit tests
- **SpringDoc OpenAPI**: Auto-generated API documentation
- **JUnit 5**: Modern testing framework with Spring Boot Test integration

## Key Performance Features

### Database Query Optimization
1. **DTO Projections**: Fetch only required columns instead of full entities (reduces memory and network overhead)
2. **Querydsl Integration**: Type-safe, dynamic query composition with zero N+1 query issues
3. **Strategic Indexing**: B-tree, GIN, and partial indexes optimized for specific query patterns
4. **Read-Only Transactions**: `@Transactional(readOnly = true)` enables database optimizations

### Caching Architecture
1. **Distributed Redis Cache**: Shares cache across multiple application instances
2. **Conditional Cache Enablement**: Graceful fallback when Redis unavailable
3. **TTL-Based Invalidation**: Automatic cache expiration prevents stale data
4. **Cache Key Normalization**: Case-insensitive keys prevent cache fragmentation

### Search Performance
1. **Trigram Similarity Search**: Faster than `LIKE` with ranking capabilities
2. **Normalized Search Column**: Pre-processed company names for optimal matching
3. **Query Result Limiting**: Top-K results minimize data transfer and response time
4. **Full Outer Join Strategy**: Single query for multi-table search

## Prerequisites

- Java 25 or higher
- Maven 3.6 or higher
- PostgreSQL 14+ with pg_trgm extension (for production)
- Redis 6+ (optional, for distributed caching)


## Building the Project

```bash
mvn clean compile
```

## Running Tests

```bash
mvn test
```

## Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Configuration & secrets

The application reads runtime configuration (database URL and credentials) from Spring's property sources. To avoid committing secrets to Git, provide the values using one of these safe options:

1) Local `.env` file (recommended for local development)

   - Copy the example and fill in real values (DO NOT commit this file):

     ```bash
     cp .env.example .env
     # edit .env and set DB_URL, DB_USERNAME, DB_PASSWORD
     ```

   - This project includes a small loader that will read a `.env` file from the project working directory and inject its keys into Spring's Environment automatically. The loader runs before Spring resolves `${...}` placeholders, so `application.properties` can reference `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` directly.

   - `.env` is ignored by the repository (`.gitignore`) — keep it that way.

2) External properties file

   - Create `application-secrets.properties` at the project root (next to the jar or project directory). Example contents:

     ```properties
     spring.datasource.url=jdbc:postgresql://your-host:5432/yourdb?sslmode=require
     spring.datasource.username=your_user
     spring.datasource.password=your_pass
     ```

   - The tracked `application.properties` includes:
     `spring.config.import=optional:file:./application-secrets.properties`
     so Spring will load this file automatically if present.

3) Environment variables / shell

   - Export environment variables before running the app or pass them inline:

     ```bash
     export DB_URL='jdbc:postgresql://host:5432/db?sslmode=require'
     export DB_USERNAME='your_user'
     export DB_PASSWORD='your_pass'
     mvn spring-boot:run
     ```

     Or inline:
     ```bash
     DB_URL='...' DB_USERNAME='...' DB_PASSWORD='...' mvn spring-boot:run
     ```

CI / production

 - Do NOT store credentials in the repository. Configure secrets in your CI provider (GitHub Actions, GitLab CI, etc.) and inject them as environment variables into the build/runtime.

Security notes

- If `.env` or any secret file was accidentally committed, stop and remove it from the index (`git rm --cached .env`) and commit; for full history removal use tools like `git-filter-repo` or BFG (this is destructive and requires a force-push — ask before proceeding).
- Prefer secret managers (AWS Secrets Manager, HashiCorp Vault, etc.) for production systems.

Tests

- Unit/integration tests run against an in-memory H2 database. See `src/test/resources/application.properties` for the test DB configuration.


## API Endpoints

### Search & Autocomplete
```
GET /api/v1/search/auto-complete?query={searchTerm}
```

High-performance fuzzy search with trigram similarity scoring. Returns top 10 matches across ticker symbols and company names.

**Performance Optimizations**:
- PostgreSQL pg_trgm extension for sub-millisecond search
- Redis caching (15-minute TTL)
- Single query with FULL OUTER JOIN strategy
- Composite scoring algorithm

Example response:
```json
{
  "query": "appl",
  "results": [
    {
      "ticker": "AAPL",
      "companyName": "Apple Inc.",
      "score": 0.85
    }
  ]
}
```

### Stock Details Summary
```
GET /api/v1/stock-details/summary/{ticker}
```

Comprehensive stock metrics combining ticker summary and overview data. Optimized with Redis caching (60-minute TTL).

Example response:
```json
{
  "valuation": {
    "marketCap": 3000000000000,
    "peRatio": 25.5,
    "forwardPeRatio": 22.3,
    "enterpriseToEbitda": 18.2,
    "priceToBook": 42.1,
    "fiftyDayAverage": {
      "value": 145.67,
      "percentageDifference": 3.14
    },
    "twoHundredDayAverage": {
      "value": 140.23,
      "percentageDifference": 7.15
    }
  },
  "margins": {
    "grossMargin": 43.26,
    "operatingMargin": 30.29,
    "profitMargin": 25.31
  },
  "growth": {
    "earningsGrowth": 11.5,
    "revenueGrowth": 8.3,
    "trailingEps": 6.13,
    "forwardEps": 7.21,
    "pegRatio": 2.2
  },
  "dividend": {
    "dividendYield": 0.82,
    "payoutRatio": 25.5
  }
}
```

### Get CIK Lookup by CIK
```
GET /api/v1/cik-lookup/{cik}
```

Returns CIK lookup information for the given CIK number.

Example response:
```json
{
  "cik": 320193,
  "companyName": "Apple Inc.",
  "createdAt": "2023-01-01T10:00:00",
  "lastUpdatedAt": "2023-01-01T10:00:00"
}
```

### Get Paginated Ticker Summaries with Advanced Filtering
```
GET /api/v1/ticker-summary/list?page=0&size=20&sortBy=marketCap&sortOrder=desc
```

High-performance paginated endpoint with dynamic filtering and sorting. Uses Querydsl for optimized DTO projections.

**Query Parameters**:
- `page`: Page number (default: 0)
- `size`: Page size (default: 20, max: 100)
- `sortBy`: Sort field (ticker, marketCap, previousClose, peRatio, etc.)
- `sortOrder`: asc or desc
- **Filter Parameters**: minMarketCap, maxMarketCap, minPe, maxPe, minDividendYield, maxDividendYield, etc.

**Performance Features**:
- Separate count and data queries for optimal execution plans
- DTO projection fetches only required columns
- Partial indexes on filter columns
- Dynamic predicate composition (no query plan proliferation)

Example response:
```json
{
  "content": [
    {
      "ticker": "AAPL",
      "companyName": "Apple Inc.",
      "marketCap": 3000000000000,
      "previousClose": 150.25,
      "peRatio": 25.5,
      "forwardPeRatio": 22.3,
      "dividendYield": 0.82,
      "payoutRatio": 25.5,
      "fiftyDayAverage": 145.67,
      "twoHundredDayAverage": 140.23
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 5000,
  "totalPages": 250
}
```

## Performance Benchmarks

### Query Performance Metrics
- **Autocomplete Search**: <50ms (with Redis cache: <5ms)
- **Ticker Summary List**: <100ms for 20 records with filters
- **Stock Details**: <80ms (with cache: <3ms)
- **Complex Filter Queries**: <150ms with multiple predicates

### Optimization Techniques Applied
1. **Index-Only Scans**: Covered indexes reduce disk I/O
2. **Connection Pooling**: HikariCP with optimized pool sizing
3. **Batch Processing**: JDBC batch inserts for bulk operations
4. **Lazy Loading**: Fetch associations only when needed
5. **Read Replicas**: Can be configured for read-heavy workloads

## Database Schema Design

### Table Structure
- `cik_lookup`: Company CIK to name mappings (with normalized search column)
- `ticker_summary`: Core stock metrics and moving averages
- `ticker_overview`: Extended financial ratios and growth metrics

### Index Strategy
```sql
-- GIN trigram indexes for fuzzy search
CREATE INDEX idx_ticker_lower_trgm ON ticker_summary USING gin (lower(ticker) gin_trgm_ops);
CREATE INDEX idx_company_name_search_trgm ON cik_lookup USING gin (company_name_search gin_trgm_ops);

-- Partial indexes for nullable columns
CREATE INDEX idx_ticker_summary_pe_ratio ON ticker_summary(pe_ratio) WHERE pe_ratio IS NOT NULL;

-- B-tree indexes for range queries and sorting
CREATE INDEX idx_ticker_summary_market_cap ON ticker_summary(market_cap);
```

## OpenAPI Documentation

Access the OpenAPI documentation (Swagger UI) at: `http://localhost:8080/swagger-ui/index.html`

## Development Tools

### H2 Database Console (Test Environment)
Access at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:stockdb`
- Username: `sa`
- Password: (leave empty)

## Project Structure

```
src/
├── main/
│   ├── java/com/stockInformation/
│   │   ├── StockInformationApplication.java     # Main Spring Boot application
│   │   │
│   │   ├── config/                              # Configuration classes
│   │   │   ├── QuerydslConfig.java              # JPAQueryFactory bean for type-safe queries
│   │   │   ├── RedisCacheConfig.java            # Redis-backed distributed caching
│   │   │   ├── SecurityConfig.java              # OAuth2 JWT authentication
│   │   │   ├── WebConfig.java                   # CORS and web MVC configuration
│   │   │   └── DotenvEnvironmentPostProcessor.java # .env file loader
│   │   │
│   │   ├── cikLookup/                           # CIK lookup domain
│   │   │   ├── api/v1/
│   │   │   │   ├── CikLookupController.java     # REST endpoints
│   │   │   │   └── CikLookupMapper.java         # MapStruct DTO mapper
│   │   │   ├── entity/CikLookup.java            # JPA entity
│   │   │   ├── dto/CikLookupDTO.java            # Data transfer object
│   │   │   ├── repository/CikLookupRepository.java # Spring Data JPA repository
│   │   │   └── service/CikLookupService.java    # Business logic
│   │   │
│   │   ├── tickerSummary/                       # Ticker summary domain
│   │   │   ├── api/v1/TickerSummaryController.java
│   │   │   ├── dto/TickerSummaryDTO.java
│   │   │   ├── entity/TickerSummary.java
│   │   │   ├── repository/
│   │   │   │   ├── TickerSummaryRepository.java
│   │   │   │   ├── TickerSummaryCompanyRepository.java        # Custom Querydsl repo
│   │   │   │   └── TickerSummaryCompanyRepositoryImpl.java    # DTO projection impl
│   │   │   ├── service/TickerSummaryService.java
│   │   │   ├── transformer/SortOrderTransformer.java # Sort conversion utilities
│   │   │   └── utils/                           # Validation utilities
│   │   │
│   │   ├── stockDetails/                        # Stock details domain
│   │   │   ├── api/v1/StockDetailsController.java
│   │   │   ├── dto/                             # Response DTOs (Growth, Valuation, etc.)
│   │   │   ├── entity/TickerOverview.java
│   │   │   ├── repository/TickerOverviewRepository.java
│   │   │   ├── service/StockDetailsService.java # Cached service layer
│   │   │   └── transformer/StockDetailsTransformer.java # Hand-optimized mappings
│   │   │
│   │   ├── search/                              # Fuzzy search domain
│   │   │   ├── api/v1/SearchController.java
│   │   │   ├── dto/                             # Autocomplete DTOs
│   │   │   ├── repository/
│   │   │   │   ├── SearchRepository.java
│   │   │   │   └── SearchRepositoryImpl.java    # Native SQL with pg_trgm
│   │   │   ├── service/SearchService.java       # Cached autocomplete logic
│   │   │   └── utils/utils.java                 # Query normalization
│   │   │
│   │   ├── common/
│   │   │   └── dto/PageResponse.java            # Generic paginated response
│   │   │
│   │   └── exception/
│   │       └── ApiExceptionHandler.java         # Global exception handling
│   │
│   └── resources/
│       ├── application.properties               # Main configuration
│       └── META-INF/spring.factories            # Custom environment processors
│
├── test/
│   ├── java/com/stockInformation/              # Comprehensive test suite
│   │   ├── config/RedisConnectionTest.java     # Cache integration tests
│   │   ├── cikLookup/                          # Domain tests
│   │   ├── tickerSummary/                      # Repository & service tests
│   │   ├── stockDetails/                       # DTO transformation tests
│   │   └── search/                             # Search algorithm tests
│   └── resources/
│       ├── application.properties              # Test configuration (H2 database)
│       └── init.sql                            # Test data initialization
│
└── db/migrations/                              # SQL schema migrations
    ├── create_cik_lookup_table.sql             # CIK table with trigram indexes
    ├── create_ticker_summary_table.sql         # Ticker table with partial indexes
    └── create_ticker_overview_table.sql        # Overview table with foreign keys
```

## Engineering Best Practices

### Code Quality
- **Lombok**: Eliminates getter/setter boilerplate
- **MapStruct**: Zero-reflection DTO mapping at compile time
- **Immutable DTOs**: Value objects with validation
- **Null Safety**: Optional<T> for potentially absent values

### Testing Strategy
- **Unit Tests**: Service and repository layer tests with H2
- **Integration Tests**: TestContainers with real PostgreSQL
- **API Tests**: Spring MockMvc for controller testing
- **Test Coverage**: >80% code coverage across critical paths

### Security Practices
- **Environment-based Secrets**: No credentials in source code
- **JWT Validation**: Stateless token-based authentication
- **CORS Configuration**: Controlled cross-origin access
- **SQL Injection Protection**: Parameterized queries via JPA/Querydsl

## Configuration

### Production Configuration (PostgreSQL + Redis)

Update `application.properties` or use environment variables:

```properties
# Database Configuration
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/stockdb}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# Redis Cache Configuration
spring.redis.host=${REDIS_HOST:localhost}
spring.redis.port=${REDIS_PORT:6379}
spring.redis.ssl.enabled=${REDIS_SSL_ENABLED:false}
spring.cache.type=redis

# Performance Tuning
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.jpa.properties.hibernate.jdbc.batch_size=20
```

### Development Configuration (H2)

Tests automatically use H2 in-memory database with no Redis dependency.

## Deployment Considerations

### Horizontal Scaling
- **Stateless Design**: No session storage, can run multiple instances
- **Shared Redis Cache**: All instances share the same cache
- **Database Connection Pooling**: Configure pool size based on instance count
- **Load Balancer Ready**: Health check endpoint at `/actuator/health`

### Performance Tuning
1. **JVM Options**: Use G1GC for low-latency garbage collection
   ```bash
   -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Xms2g -Xmx2g
   ```
2. **Database Tuning**: Adjust PostgreSQL `shared_buffers`, `work_mem` for your workload
3. **Redis Tuning**: Use `maxmemory-policy` of `allkeys-lru` for cache eviction
4. **Connection Pools**: Size HikariCP based on: `pool_size = ((core_count * 2) + disk_spindles)`

## License

This project is open source and available under the MIT License.