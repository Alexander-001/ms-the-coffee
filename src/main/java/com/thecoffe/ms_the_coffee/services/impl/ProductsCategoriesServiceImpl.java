package com.thecoffe.ms_the_coffee.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thecoffe.ms_the_coffee.models.ProductsCategories;
import com.thecoffe.ms_the_coffee.repositories.ProductsCategoriesRepository;
import com.thecoffe.ms_the_coffee.services.interfaces.ProductsCategoriesService;

@Service
public class ProductsCategoriesServiceImpl implements ProductsCategoriesService {

    @Autowired
    private ProductsCategoriesRepository productsCategoriesRepository;

    // * Get all products categories
    @Transactional(readOnly = true)
    @Override
    public List<ProductsCategories> findAll() {
        return (List<ProductsCategories>) productsCategoriesRepository.findAll();
    }

    // * Get category by id
    @Transactional(readOnly = true)
    @Override
    public Optional<ProductsCategories> findById(Long id) {
        return productsCategoriesRepository.findById(id);
    }

    // * Validate if exists category by name in database
    @Transactional
    @Override
    public boolean existsByName(String name) {
        return productsCategoriesRepository.existsByName(name);
    }

    // * Save new product category in database
    @Transactional()
    @Override
    public ProductsCategories save(ProductsCategories productsCategories) {
        return productsCategoriesRepository.save(productsCategories);
    }

    // * Get product category by id and update with new data
    @Transactional
    @Override
    public Optional<ProductsCategories> update(Long id, ProductsCategories productsCategories) {
        Optional<ProductsCategories> prodCategoryDb = productsCategoriesRepository.findById(id);
        if (prodCategoryDb.isPresent()) {
            ProductsCategories updateCategory = prodCategoryDb.get();
            updateCategory.setName(productsCategories.getName());
            return Optional.of(productsCategoriesRepository.save(updateCategory));
        }
        return prodCategoryDb;
    }

    // * Delete category by id
    @Transactional
    @Override
    public Optional<ProductsCategories> delete(Long id) {
        Optional<ProductsCategories> category = productsCategoriesRepository.findById(id);
        category.ifPresent(cat -> {
            productsCategoriesRepository.delete(cat);
        });
        return category;
    }

}
