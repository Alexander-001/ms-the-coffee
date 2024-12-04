package com.thecoffe.ms_the_coffee.validations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thecoffe.ms_the_coffee.services.interfaces.ProductsCategoriesService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistsCategoryValidation implements ConstraintValidator<ExistsCategory, String> {

    @Autowired
    private ProductsCategoriesService productsCategoriesService;

    @Override
    public boolean isValid(String categoryName, ConstraintValidatorContext context) {
        if (productsCategoriesService == null) {
            return true;
        }
        return productsCategoriesService.existsByName(categoryName);
    }

}
