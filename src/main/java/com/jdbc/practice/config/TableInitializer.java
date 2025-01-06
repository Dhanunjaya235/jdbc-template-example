package com.jdbc.practice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class TableInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        String query = "CREATE TABLE IF NOT EXISTS products (id SERIAL PRIMARY KEY,  name VARCHAR(255),  price DECIMAL(10,2))";
        String query2 = "CREATE TABLE IF NOT EXISTS files (id SERIAL PRIMARY KEY,  file_name VARCHAR(255),  file BYTEA)";
        jdbcTemplate.execute(query);
        jdbcTemplate.execute(query2);

        String updateFilesStoredProcedure = """
                CREATE OR REPLACE PROCEDURE update_file(file_id INT, file_bytes bytea, updated_file_name VARCHAR(255))
                LANGUAGE plpgsql
                AS $$
                BEGIN
                    UPDATE files
                    SET file = file_bytes, file_name = updated_file_name
                    WHERE id = file_id;
                END;
                $$;
                """;
        jdbcTemplate.execute(updateFilesStoredProcedure);
    }
}
