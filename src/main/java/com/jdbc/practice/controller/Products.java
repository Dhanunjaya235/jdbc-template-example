package com.jdbc.practice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jdbc.practice.model.Product;
import com.jdbc.practice.service.ProductService;

@RestController
@RequestMapping("/products")
public class Products {

    @Autowired
    private ProductService productService;

    @GetMapping("/get-all")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/get/{id}")
    public Product getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<Product> insertAProduct(@RequestBody Product reqProduct) {
        int id = productService.insertAProduct(reqProduct.getName(), reqProduct.getPrice());
        reqProduct.setId(id);
        return new ResponseEntity<>(reqProduct, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAProduct(@PathVariable int id) {
        productService.deleteAProduct(id);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateAProduct(@PathVariable int id, @RequestBody Product reqProduct) {
        try {
            return new ResponseEntity<>(productService.updateAProduct(id, reqProduct), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Product update failed",
                            "message", e.getMessage(),
                            "timestamp", System.currentTimeMillis()));
        }
    }

}
