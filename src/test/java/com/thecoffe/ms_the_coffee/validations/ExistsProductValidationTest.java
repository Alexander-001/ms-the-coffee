package com.thecoffe.ms_the_coffee.validations;

import com.thecoffe.ms_the_coffee.services.interfaces.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ExistsProductValidationTest {


    @InjectMocks
    private ExistsProductValidation existsProductValidation;

    @Mock
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isValidProductExists() {
        when(productService.existsBySku("1234")).thenReturn(true);
        boolean isValid = existsProductValidation.isValid("1234", null);
        assertFalse(isValid, "Validacion se cae cuando el sku existe");
    }

    @Test
    void isValidProductNotExists() {
        when(productService.existsBySku("1234")).thenReturn(false);
        boolean isValid = existsProductValidation.isValid("1234", null);
        assertTrue(isValid, "Validacion se cae cuando el sku no existe");
    }

    @Test
    void isValidProductNull() {
        existsProductValidation = new ExistsProductValidation();
        boolean isValid = existsProductValidation.isValid("1234", null);
        assertTrue(isValid, "Validacion se cae cuando el sku es null");
    }
}