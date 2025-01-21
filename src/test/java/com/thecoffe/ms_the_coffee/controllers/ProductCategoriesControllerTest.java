package com.thecoffe.ms_the_coffee.controllers;

import com.thecoffe.ms_the_coffee.models.ProductsCategories;
import com.thecoffe.ms_the_coffee.services.interfaces.ProductService;
import com.thecoffe.ms_the_coffee.services.interfaces.ProductsCategoriesService;
import com.thecoffe.ms_the_coffee.validations.ValidationBindingResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ProductCategoriesControllerTest {


    @Mock
    private ProductsCategoriesService productsCategoriesService;

    @InjectMocks
    private ProductCategoriesController productCategoriesController;

    @Mock
    private ValidationBindingResult validationBindingResult;

    @Mock
    private BindingResult bindingResult;


    private ProductsCategories productsCategories;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productsCategories = new ProductsCategories();
        productsCategories.setId(1L);
        productsCategories.setName("test");
        productsCategories.setDescription("description");
        validationBindingResult = new ValidationBindingResult();
    }

    @Test
    void findAllCategories() {
        when(productsCategoriesService.findAll()).thenReturn(Collections.singletonList(productsCategories));
        ResponseEntity<Map<String, Object>> response = productCategoriesController.findAllCategories();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void notFindCategoryById() {
        when(productsCategoriesService.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Map<String, Object>> response = productCategoriesController.findCategoryById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findCategoryById() {
        when(productsCategoriesService.findById(1L)).thenReturn(Optional.of(productsCategories));
        ResponseEntity<Map<String, Object>> response = productCategoriesController.findCategoryById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void notCreateCategory() {
        isErrorFields();
        ResponseEntity<Map<String, Object>> response = productCategoriesController.createCategory(productsCategories, bindingResult);
        assertNull(response);
    }

    @Test
    void createCategory() {
        when(productsCategoriesService.save(productsCategories)).thenReturn(productsCategories);
        ResponseEntity<Map<String, Object>> response = productCategoriesController.createCategory(productsCategories, bindingResult);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void notUpdateCategoryErrors() {
        isErrorFields();
        ResponseEntity<Map<String, Object>> response = productCategoriesController.updateCategory(productsCategories, bindingResult, 1L);
        assertNull(response);
    }

    @Test
    void notUpdateCategory() {
        when(productsCategoriesService.update(1L, productsCategories)).thenReturn(Optional.empty());
        ResponseEntity<Map<String, Object>> response = productCategoriesController.updateCategory(productsCategories, bindingResult, 1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateCategory() {
        when(productsCategoriesService.update(1L, productsCategories)).thenReturn(Optional.of(productsCategories));
        ResponseEntity<Map<String, Object>> response = productCategoriesController.updateCategory(productsCategories, bindingResult, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void notDeleteCategory() {
        when(productsCategoriesService.delete(1L)).thenReturn(Optional.empty());
        ResponseEntity<Map<String, Object>> response = productCategoriesController.deleteProduct(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteCategory() {
        when(productsCategoriesService.delete(1L)).thenReturn(Optional.of(productsCategories));
        ResponseEntity<Map<String, Object>> response = productCategoriesController.deleteProduct(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    void isErrorFields() {
        when(bindingResult.hasFieldErrors()).thenReturn(true);
        when(validationBindingResult.validation(bindingResult)).thenReturn(null);
    }
}