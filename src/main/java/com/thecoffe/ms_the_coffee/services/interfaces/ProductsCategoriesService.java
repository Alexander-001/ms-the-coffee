package com.thecoffe.ms_the_coffee.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.thecoffe.ms_the_coffee.models.ProductsCategories;

public interface ProductsCategoriesService {
    List<ProductsCategories> findAll();

    Optional<ProductsCategories> findById(Long id);

    ProductsCategories save(ProductsCategories productsCategories);

    Optional<ProductsCategories> update(Long id, ProductsCategories productsCategories);

    Optional<ProductsCategories> delete(Long id);

    boolean existsByName(String name);
}
