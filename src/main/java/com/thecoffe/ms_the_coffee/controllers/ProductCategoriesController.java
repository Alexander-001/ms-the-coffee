package com.thecoffe.ms_the_coffee.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thecoffe.ms_the_coffee.models.ProductsCategories;
import com.thecoffe.ms_the_coffee.services.interfaces.ProductsCategoriesService;
import com.thecoffe.ms_the_coffee.validations.ValidationBindingResult;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/products-categories")
public class ProductCategoriesController {

    @Autowired
    private ProductsCategoriesService productsCategoriesService;

    @Autowired
    private ValidationBindingResult validationBindingResult;

    // * Get all categories
    @GetMapping
    public ResponseEntity<Map<String, Object>> findAllCategories() {
        List<ProductsCategories> categories = productsCategoriesService.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Categorias encontradas");
        response.put("categories", categories);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // * Get category by id
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> findCategoryById(@PathVariable Long id) {
        Optional<ProductsCategories> categoryDb = productsCategoriesService.findById(id);
        Map<String, Object> response = new HashMap<>();
        if (categoryDb.isPresent()) {
            response.put("message", "Categoria encontrada");
            response.put("category", categoryDb.orElseThrow());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("message", "No existe la categoria");
        response.put("category", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // * Create a new category
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCategory(@Valid @RequestBody ProductsCategories productsCategories,
            BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationBindingResult.validation(result);
        }
        ProductsCategories newCategory = productsCategoriesService.save(productsCategories);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Categoria creada correctamente");
        response.put("category", newCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // * Update category by id
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCategory(@Valid @RequestBody ProductsCategories productsCategories,
            BindingResult result, @PathVariable Long id) {
        if (result.hasFieldErrors()) {
            return validationBindingResult.validation(result);
        }
        Optional<ProductsCategories> updateCategory = productsCategoriesService.update(id, productsCategories);
        Map<String, Object> response = new HashMap<>();
        if (updateCategory.isPresent()) {
            productsCategories.setId(id);
            response.put("message", "Categoria actualizada");
            response.put("category", updateCategory.orElseThrow());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("message", "No se pudo actualizar categoria");
        response.put("category", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // * Delete category by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long id) {
        Optional<ProductsCategories> categoryDelete = productsCategoriesService.delete(id);
        Map<String, Object> response = new HashMap<>();
        if (categoryDelete.isPresent()) {
            response.put("message", "Categoria eliminada");
            response.put("category", categoryDelete.orElseThrow());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("message", "No se pudo eliminar categoria");
        response.put("category", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
