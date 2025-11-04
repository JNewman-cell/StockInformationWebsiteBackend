# Stock Information Website Backend

A Java backend application built with Spring Boot, JPA, and Maven for managing stock information.

## Technologies Used

- **Java 17**: Programming language
- **Spring Boot 3.2.0**: Framework for building the application
- **Spring Data JPA**: For database operations using JPA
- **Maven**: Build and dependency management tool
- **H2 Database**: In-memory database for development and testing
- **Hibernate**: JPA implementation for ORM

## Features

- RESTful API for stock information management
- CRUD operations for stocks
- JPA/Hibernate for database persistence
- H2 in-memory database with console access
- Input validation
- Comprehensive test coverage

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

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

## API Endpoints

### Get All Stocks
```
GET /api/stocks
```

### Get Stock by ID
```
GET /api/stocks/{id}
```

### Get Stock by Symbol
```
GET /api/stocks/symbol/{symbol}
```

### Get Stocks by Sector
```
GET /api/stocks/sector/{sector}
```

### Create Stock
```
POST /api/stocks
Content-Type: application/json

{
  "symbol": "AAPL",
  "companyName": "Apple Inc.",
  "currentPrice": 150.00,
  "previousClose": 148.50,
  "dayHigh": 151.00,
  "dayLow": 149.00,
  "volume": 50000000,
  "sector": "Technology"
}
```

### Update Stock
```
PUT /api/stocks/{id}
Content-Type: application/json

{
  "symbol": "AAPL",
  "companyName": "Apple Inc.",
  "currentPrice": 155.00,
  "previousClose": 150.00,
  "dayHigh": 156.00,
  "dayLow": 154.00,
  "volume": 45000000,
  "sector": "Technology"
}
```

### Delete Stock
```
DELETE /api/stocks/{id}
```

## H2 Database Console

Access the H2 console at: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:stockdb`
- Username: `sa`
- Password: (leave empty)

## Project Structure

```
src/
├── main/
│   ├── java/com/stockinfo/backend/
│   │   ├── StockInformationApplication.java  # Main application class
│   │   ├── entity/
│   │   │   └── Stock.java                    # JPA Entity
│   │   ├── repository/
│   │   │   └── StockRepository.java          # JPA Repository
│   │   ├── service/
│   │   │   └── StockService.java             # Business logic
│   │   └── controller/
│   │       └── StockController.java          # REST API controller
│   └── resources/
│       └── application.properties             # Application configuration
└── test/
    └── java/com/stockinfo/backend/
        ├── StockInformationApplicationTests.java
        └── repository/
            └── StockRepositoryTest.java       # Repository tests
```

## Configuration

The application uses H2 in-memory database by default. To use a different database, update the `application.properties` file with appropriate database connection details.

## License

This project is open source and available under the MIT License.