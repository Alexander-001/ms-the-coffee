package com.thecoffe.ms_the_coffee.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = NotExistsCategoryValidation.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotExistsCategory {
    String message() default "La categoría no existe.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
