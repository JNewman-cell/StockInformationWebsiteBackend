# Stock Information Website Backend

A Java backend application built with Spring Boot, JPA, and Maven for managing stock information.

## Technologies Used

- **Java 21**: Programming language
- **Spring Boot 3.2.0**: Framework for building the application
- **Spring Data JPA**: For database operations using JPA
- **PostgreSQL**: Database for production
- **H2 Database**: In-memory database for development and testing
- **Hibernate**: JPA implementation for ORM
- **MapStruct**: For compile-time mapper generation
- **SpringDoc OpenAPI**: For API documentation

## Features

- RESTful API for stock information retrieval
- JPA/Hibernate for database persistence
- H2 in-memory database with console access
- Input validation
- Comprehensive test coverage
- OpenAPI documentation

## Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- PostgreSQL (for production)

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

### Get All Ticker Summaries
```
GET /api/v1/ticker-summary?page=0&size=20
```

Returns paginated list of all ticker summaries.

Query parameters:
- `page`: Page number (default: 0)
- `size`: Page size (default: 20)

Example response:
```json
{
  "content": [
    {
      "ticker": "AAPL",
      "cik": 320193,
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
  "totalElements": 100,
  "totalPages": 5
}
```

### Get Ticker Summary by Ticker
```
GET /api/v1/ticker-summary/{ticker}
```

Returns ticker summary for the given ticker symbol (case-insensitive).

Example response:
```json
{
  "ticker": "AAPL",
  "cik": 320193,
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
```

## H2 Database Console

Access the H2 console at: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:stockdb`
- Username: `sa`
- Password: (leave empty)

## OpenAPI Documentation

Access the OpenAPI documentation (Swagger UI) at: `http://localhost:8080/swagger-ui/index.html` (the starter may also redirect `/swagger-ui.html`)

## Project Structure

```
src/
├── main/
│   ├── java/com/stockinfo/backend/
│   │   ├── StockInformationApplication.java  # Main application class
│   │   ├── api/v1/
│   │   │   ├── cik/
│   │   │   │   ├── CikLookupController.java    # CIK lookup API
│   │   │   │   ├── CikLookupDTO.java           # CIK lookup DTO
│   │   │   │   └── CikLookupMapper.java        # CIK lookup mapper
│   │   │   └── ticker/
│   │   │       ├── TickerSummaryController.java # Ticker summary API
│   │   │       ├── TickerSummaryDTO.java       # Ticker summary DTO
│   │   │       └── TickerSummaryMapper.java    # Ticker summary mapper
│   │   ├── config/
│   │   │   └── WebConfig.java                  # Web configuration
│   │   ├── entity/
│   │   │   ├── CikLookup.java                  # CIK lookup entity
│   │   │   └── TickerSummary.java              # Ticker summary entity
│   │   ├── exception/
│   │   │   └── ApiExceptionHandler.java        # Global exception handler
│   │   ├── repository/
│   │   │   ├── CikLookupRepository.java        # CIK lookup repository
│   │   │   └── TickerSummaryRepository.java    # Ticker summary repository
│   │   └── service/
│   │       ├── CikLookupService.java           # CIK lookup service
│   │       └── TickerSummaryService.java       # Ticker summary service
│   └── resources/
│       └── application.properties               # Application configuration
└── test/
    └── java/com/stockinfo/backend/
        ├── StockInformationApplicationTests.java
        ├── api/v1/cik/
        │   └── CikLookupControllerTest.java     # CIK controller tests
        ├── api/v1/ticker/
        │   └── TickerSummaryControllerTest.java # Ticker controller tests
        ├── repository/
        │   ├── CikLookupRepositoryTest.java     # CIK repository tests
        │   └── TickerSummaryRepositoryTest.java # Ticker repository tests
        └── service/
            ├── CikLookupServiceTest.java        # CIK service tests
            └── TickerSummaryServiceTest.java    # Ticker service tests
```

## Configuration

The application uses H2 in-memory database by default. To use PostgreSQL, update the `application.properties` file:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/stockdb
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## License

This project is open source and available under the MIT License.