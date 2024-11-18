package com.thecoffe.ms_the_coffee.repositories;

import org.springframework.data.repository.CrudRepository;

import com.thecoffe.ms_the_coffee.models.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
    boolean existsBySku(String sku);
}
