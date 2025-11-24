package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

@Component
@Slf4j
public class PostgresqlDriver implements CommandLineRunner {

    private final DataSource dataSource;

    @Value("classpath:sql/ddl/01_schema.sql")
    private Resource schemaScript;

    @Value("classpath:sql/ddl/02_procedures.sql")
    private Resource procedureScript;

    public PostgresqlDriver(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeDatabase();
    }

    public void initializeDatabase() {
        try {
            executeScript(schemaScript);
            executeScript(procedureScript);
            log.info("Database initialized from SQL scripts");
        } catch (Exception e) {
            log.error("Error initializing database", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    private void executeScript(Resource script) throws Exception {
        if (script == null || !script.exists()) {
            log.warn("Script not found or is null: {}", script != null ? script.getFilename() : "null");
            return;
        }

        String sql = readResource(script);
        String[] statements = sql.split(";(?=(?:[^$]*\\$\\$[^$]*\\$\\$)*[^$]*$)");

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            for (String statement : statements) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("--") && !trimmed.startsWith("/*")) {
                    try {
                        stmt.execute(trimmed);
                        log.debug("Executed SQL statement successfully");
                    } catch (Exception e) {
                        log.warn("Could not execute statement: {}", e.getMessage());
                    }
                }
            }
        }
    }

    private String readResource(Resource resource) throws Exception {
        try (Scanner scanner = new Scanner(resource.getInputStream())) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    private Connection currentConnection;

    public void beginTransaction() {
        try {
            currentConnection = dataSource.getConnection();
            currentConnection.setAutoCommit(false);
            log.debug("Transaction started");
        } catch (Exception e) {
            throw new RuntimeException("Error starting transaction", e);
        }
    }

    public void commit() {
        try {
            if (currentConnection != null && !currentConnection.isClosed()) {
                currentConnection.commit();
                currentConnection.setAutoCommit(true);
                currentConnection.close();
                log.debug("Transaction committed");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error committing transaction", e);
        } finally {
            currentConnection = null;
        }
    }

    public void rollback() {
        try {
            if (currentConnection != null && !currentConnection.isClosed()) {
                currentConnection.rollback();
                currentConnection.setAutoCommit(true);
                currentConnection.close();
                log.debug("Transaction rolled back");
            }
        } catch (Exception e) {
            log.error("Error during rollback: {}", e.getMessage());
        } finally {
            currentConnection = null;
        }
    }
}