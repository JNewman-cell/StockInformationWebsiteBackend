package com.stockInformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@EnableCaching
public class StockInformationApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockInformationApplication.class, args);
    }

    @Bean
    public CommandLineRunner warmupDatabase(@Autowired(required = false) JdbcTemplate jdbcTemplate) {
        return args -> {
            if (jdbcTemplate != null) {
                // Simple query to warm up the database connection
                jdbcTemplate.execute("SELECT 1");
                System.out.println("Database connection warmed up successfully.");
            }
        };
    }
}
