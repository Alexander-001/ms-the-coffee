package com.thecoffe.ms_the_coffee.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thecoffe.ms_the_coffee.models.Product;
import com.thecoffe.ms_the_coffee.repositories.ProductRepository;
import com.thecoffe.ms_the_coffee.services.interfaces.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    // * Get all products
    @Transactional(readOnly = true)
    @Override
    public List<Product> findAll() {
        return (List<Product>) productRepository.findAll();
    }

    // * Get product by id
    @Transactional(readOnly = true)
    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    // * Validate if exists product by sku in database
    @Transactional(readOnly = true)
    @Override
    public boolean existsBySku(String sku) {
        return productRepository.existsBySku(sku);
    }

    // * Save new product in database
    @Transactional
    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    // * Get product by id and update with new data
    @Override
    public Optional<Product> update(Long id, Product product) {
        Optional<Product> productDb = productRepository.findById(id);
        if (productDb.isPresent()) {
            Product updateProduct = productDb.get();
            updateProduct.setName(product.getName());
            updateProduct.setPrice(product.getPrice());
            updateProduct.setDescription(product.getDescription());
            updateProduct.setSku(product.getSku());
            updateProduct.setImage(product.getImage());
            updateProduct.setCategory(product.getCategory());
            return Optional.of(productRepository.save(updateProduct));
        }
        return productDb;
    }

    // * Delete product by id
    @Transactional
    @Override
    public Optional<Product> delete(Long id) {
        Optional<Product> productDb = productRepository.findById(id);
        productDb.ifPresent((prod) -> {
            productRepository.delete(prod);
        });
        return productDb;
    }

}
