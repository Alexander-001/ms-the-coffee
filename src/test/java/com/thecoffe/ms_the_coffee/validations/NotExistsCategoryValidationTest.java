package com.thecoffe.ms_the_coffee.validations;

import com.thecoffe.ms_the_coffee.services.interfaces.ProductsCategoriesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class NotExistsCategoryValidationTest {


    @InjectMocks
    private NotExistsCategoryValidation notExistsCategoryValidation;

    @Mock
    private ProductsCategoriesService productsCategoriesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isValidNotCategoryExists() {
        when(productsCategoriesService.existsByName("ventas")).thenReturn(true);
        boolean isValid = notExistsCategoryValidation.isValid("ventas", null);
        assertTrue(isValid, "Validacion se cae cuando la categoria existe");
    }

    @Test
    void isValidNotCategoryNotExists() {
        when(productsCategoriesService.existsByName("ventas")).thenReturn(false);
        boolean isValid = notExistsCategoryValidation.isValid("ventas", null);
        assertFalse(isValid, "Validacion se cae cuando la categoria no existe");
    }

    @Test
    void isValidNotCategoryNull() {
        notExistsCategoryValidation = new NotExistsCategoryValidation();
        boolean isValid = notExistsCategoryValidation.isValid("ventas", null);
        assertTrue(isValid, "Validacion se cae cuando la categoria es null");
    }
}