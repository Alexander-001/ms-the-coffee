package com.thecoffe.ms_the_coffee.validations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thecoffe.ms_the_coffee.services.interfaces.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistsByEmailValidation implements ConstraintValidator<ExistsByEmail, String> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (userService == null) {
            return true;
        }
        return !userService.existsByEmail(email);
    }

}
