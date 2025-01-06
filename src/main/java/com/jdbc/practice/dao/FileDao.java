package com.jdbc.practice.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jdbc.practice.model.File;

@Repository
public class FileDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Connection connection;

    public String uploadFile(byte[] file, String fileName) {
        String query = "INSERT INTO files (file_name, file) VALUES (?,?)";
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, fileName);
                ps.setBytes(2, file);
                return ps;
            });
            return fileName + " uploaded successfully";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public File downloadFile(int id) {
        try {
            String query = "SELECT id,file_name, file FROM files WHERE id = ?";
            RowMapper<File> rowMapper = new BeanPropertyRowMapper<>(File.class);
            return jdbcTemplate.queryForObject(query, rowMapper, id);
        } catch (Exception e) {
            throw new RuntimeException("File not found");
        }
    }

    public String getFileName(int id) {
        String query = "SELECT file_name FROM files WHERE id = ?";
        return jdbcTemplate.queryForObject(query, String.class, id);
    }

    public String updateUploadedFile(int id, byte[] file, String fileName) {
        try {
            String existingFileName = getFileName(id);
            if (existingFileName == null) {
                return "File not found";
            }

            String query = "CALL update_file(?,?,?)";
            CallableStatement statement = connection.prepareCall(query);
            statement.setInt(1, id);
            statement.setBytes(2, file);
            statement.setString(3, fileName);

            statement.execute();

            return "File updated successfully";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(int id) {
        String existingFileName = getFileName(id);
        if (existingFileName == null) {
            throw new RuntimeException("File not found");
        }
        String query = "DELETE FROM files WHERE id = ?";
        jdbcTemplate.update(query, id);
    }

}
