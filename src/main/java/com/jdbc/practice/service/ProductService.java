package com.jdbc.practice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jdbc.practice.dao.ProductDao;
import com.jdbc.practice.model.Product;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    @Transactional
    public int insertAProduct(String name, Double price) {
        return productDao.insertAProduct(name, price);
    }

    @Transactional
    public void deleteAProduct(int id) {
        Product existingProduct = productDao.getProductById(id);
        if (existingProduct == null) {
            throw new RuntimeException("Product not found");
        }
        productDao.deleteAProduct(id);
    }

    @Transactional
    public Product updateAProduct(int id, Product product) {
        Product existingProduct = productDao.getProductById(id);
        if (existingProduct == null) {
            throw new RuntimeException("Product not found");
        }

        if (existingProduct.getName() != null) {
            existingProduct.setName(product.getName());
        }
        if (existingProduct.getPrice() != null) {
            existingProduct.setPrice(product.getPrice());
        }

        return productDao.updateAProduct(existingProduct);
    }

    public Product getProductById(int id) {
        return productDao.getProductById(id);
    }

}
