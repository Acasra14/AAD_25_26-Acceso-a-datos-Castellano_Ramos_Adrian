package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class DemoApplication implements CommandLineRunner {


    private final PostgresqlDriver postgresqlDriver;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Testing JDBC connection...");
        try (Connection conn = postgresqlDriver.getConnection()) {
            log.info("Connection successful: {}",
                    conn.getMetaData().getURL());
            log.info("Database: {}",
                    conn.getMetaData().getDatabaseProductName());

            postgresqlDriver.init();

        } catch (Exception e) {
            log.error("Connection failed: {}", e.getMessage());
        }
    }
}

