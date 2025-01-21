package com.thecoffe.ms_the_coffee.controllers;

import com.thecoffe.ms_the_coffee.models.Product;
import com.thecoffe.ms_the_coffee.services.interfaces.ProductService;
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

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ValidationBindingResult validationBindingResult;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private ProductController productController;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1L);
        product.setName("test");
        product.setPrice(1000);
        product.setDescription("description");
        product.setSku("12345");
        product.setImage("image");
        product.setCategory("category");
        product.setStock(10);

        validationBindingResult = new ValidationBindingResult();
    }

    @Test
    void findAllProducts() {
        when(productService.findAll()).thenReturn(Collections.singletonList(product));
        ResponseEntity<Map<String, Object>> response = productController.findAllProducts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void notFindProductById() {
        when(productService.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Map<String, Object>> response = productController.findProductById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findProductById() {
        when(productService.findById(1L)).thenReturn(Optional.of(product));
        ResponseEntity<Map<String, Object>> response = productController.findProductById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void notCreateProduct() {
        isErrorFields();
        ResponseEntity<Map<String, Object>> response = productController.createProduct(product, bindingResult);
        assertNull(response);
    }

    @Test
    void createProduct() {
        when(productService.save(product)).thenReturn(product);
        ResponseEntity<Map<String, Object>> response = productController.createProduct(product, bindingResult);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void notUpdateProductErrors() {
        isErrorFields();
        ResponseEntity<Map<String, Object>> response = productController.updateProduct(product, bindingResult, 1L);
        assertNull(response);
    }

    @Test
    void notUpdateProduct() {
        when(productService.update(1L, product)).thenReturn(Optional.empty());
        ResponseEntity<Map<String, Object>> response = productController.updateProduct(product, bindingResult, 1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateProduct() {
        when(productService.update(1L, product)).thenReturn(Optional.of(product));
        ResponseEntity<Map<String, Object>> response = productController.updateProduct(product, bindingResult, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void notDeleteProduct() {
        when(productService.delete(1L)).thenReturn(Optional.empty());
        ResponseEntity<Map<String, Object>> response = productController.deleteProduct(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteProduct() {
        when(productService.delete(1L)).thenReturn(Optional.of(product));
        ResponseEntity<Map<String, Object>> response = productController.deleteProduct(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    void isErrorFields() {
        when(bindingResult.hasFieldErrors()).thenReturn(true);
        when(validationBindingResult.validation(bindingResult)).thenReturn(null);
    }
}