package com.thecoffe.ms_the_coffee.validations;

import org.springframework.beans.factory.annotation.Autowired;

import com.thecoffe.ms_the_coffee.services.ProductService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExistsProductValidation implements ConstraintValidator<ExistsProduct, String> {

    @Autowired
    private ProductService productService;

    @Override
    public boolean isValid(String sku, ConstraintValidatorContext context) {
        if (productService == null) {
            return true;
        }
        return !productService.existsBySku(sku);
    }

}
