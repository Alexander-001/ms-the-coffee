package com.thecoffe.ms_the_coffee.services.impl;

import com.thecoffe.ms_the_coffee.models.ProductsCategories;
import com.thecoffe.ms_the_coffee.repositories.ProductsCategoriesRepository;
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

class ProductsCategoriesServiceImplTest {


    @Mock
    private ProductsCategoriesRepository productsCategoriesRepository;

    @InjectMocks
    private ProductsCategoriesServiceImpl productsCategoriesService;

    private ProductsCategories productsCategories;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productsCategories = new ProductsCategories();
        productsCategories.setId(1L);
        productsCategories.setName("name");
        productsCategories.setDescription("description");
    }

    @Test
    void findAll() {
        when(productsCategoriesRepository.findAll()).thenReturn(Collections.singletonList(productsCategories));
        assertEquals(productsCategoriesService.findAll(), Collections.singletonList(productsCategories));
    }

    @Test
    void findById() {
        when(productsCategoriesRepository.findById(1L)).thenReturn(Optional.of(productsCategories));
        assertEquals(productsCategoriesService.findById(1L), Optional.of(productsCategories));
    }

    @Test
    void existsByName() {
        when(productsCategoriesRepository.existsByName("name")).thenReturn(true);
        assertTrue(productsCategoriesService.existsByName("name"));
    }

    @Test
    void save() {
        when(productsCategoriesRepository.save(productsCategories)).thenReturn(productsCategories);
        assertEquals(productsCategoriesService.save(productsCategories), productsCategories);
    }

    @Test
    void update() {
        when(productsCategoriesRepository.findById(1L)).thenReturn(Optional.of(productsCategories));
        when(productsCategoriesRepository.save(productsCategories)).thenReturn(productsCategories);
        assertEquals(productsCategoriesService.update(1L, productsCategories), Optional.of(productsCategories));
    }

    @Test
    void delete() {
        when(productsCategoriesRepository.findById(1L)).thenReturn(Optional.of(productsCategories));
        doNothing().when(productsCategoriesRepository).delete(productsCategories);
        assertEquals(productsCategoriesService.delete(1L), Optional.of(productsCategories));
    }
}