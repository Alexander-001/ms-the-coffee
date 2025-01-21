package com.thecoffe.ms_the_coffee.services.impl;

import com.thecoffe.ms_the_coffee.models.Product;
import com.thecoffe.ms_the_coffee.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1L);
        product.setName("test");
        product.setPrice(1000);
        product.setDescription("description");
        product.setSku("123");
        product.setImage("image");
        product.setCategory("category");
        product.setStock(10);
    }

    @Test
    void findAll() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));
        assertEquals(productService.findAll(), Collections.singletonList(product));
    }

    @Test
    void findById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        assertEquals(productService.findById(1L), Optional.of(product));
    }

    @Test
    void existsBySku() {
        when(productRepository.existsBySku("123")).thenReturn(true);
        assertTrue(productService.existsBySku("123"));
    }

    @Test
    void save(){
        when(productRepository.save(product)).thenReturn(product);
        assertEquals(productService.save(product), product);
    }

    @Test
    void update(){
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        assertEquals(productService.update(1L, product), Optional.of(product));
    }

    @Test
    void delete(){
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);
        assertEquals(productService.delete(1L), Optional.of(product));
    }
}