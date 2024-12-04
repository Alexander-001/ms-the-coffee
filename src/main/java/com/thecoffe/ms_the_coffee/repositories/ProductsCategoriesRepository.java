package com.thecoffe.ms_the_coffee.repositories;

import org.springframework.data.repository.CrudRepository;

import com.thecoffe.ms_the_coffee.models.ProductsCategories;

public interface ProductsCategoriesRepository extends CrudRepository<ProductsCategories, Long> {

    boolean existsByName(String name);
}
