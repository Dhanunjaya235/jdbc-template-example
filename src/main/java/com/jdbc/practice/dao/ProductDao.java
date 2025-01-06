package com.jdbc.practice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.jdbc.practice.model.Product;

@Repository
public class ProductDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private Connection connection;

    public List<Product> getAllProducts() {
        String query = "SELECT * FROM products ORDER BY id";
        RowMapper<Product> rowMapper = new BeanPropertyRowMapper<>(Product.class);
        return jdbcTemplate.query(query, rowMapper);
    }

    public int insertAProduct(String name, Double price) {
        String query = "INSERT INTO products (name,price) VALUES (?,?)";
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query, new String[] { "id", "name" });
                ps.setString(1, name);
                ps.setDouble(2, price);
                return ps;
            }, keyHolder);
            Map<String, Object> keys = keyHolder.getKeys();
            if (keys != null && keys.containsKey("id")) {
                return (int) keys.get("id"); // Return the key of the inserted product
            } else {
                return -1; // Return -1 if no key is returned
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAProduct(int id) {
        String query = "DELETE FROM products WHERE id = ?";

        try {
            Product existingProduct = getProductById(id);
            if (existingProduct == null) {
                throw new RuntimeException("Product not found");
            }

            PreparedStatement ps = connection.prepareStatement(query, new String[] { "id" });
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            System.out.println("Deleted " + result + " product(s)");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Product getProductById(int id) {
        try {
            String query = "SELECT * FROM products WHERE id = :id";
            Map<String, Object> params = Map.of("id", id);
            RowMapper<Product> rowMapper = new BeanPropertyRowMapper<>(Product.class);
            return namedParameterJdbcTemplate.queryForObject(query, params, rowMapper);
        } catch (Exception e) {
            return null;
        }
    }

    public Product updateAProduct(Product existingProduct) {
        String query = "UPDATE products SET name = :name, price = :price WHERE id = :id";
        Product productToUpdate = getProductById(existingProduct.getId());
        if (productToUpdate == null) {
            throw new RuntimeException("Product not found");
        }

        Map<String, Object> params = Map.of("id", existingProduct.getId());
        if (existingProduct.getName() != null) {
            params.put("name", existingProduct.getName());
        } else {
            params.put("name", productToUpdate.getName());
        }
        if (existingProduct.getPrice() != null) {
            params.put("price", existingProduct.getPrice());
        } else {
            params.put("price", productToUpdate.getPrice());
        }
        namedParameterJdbcTemplate.update(query, params);
        return existingProduct;
    }
}
