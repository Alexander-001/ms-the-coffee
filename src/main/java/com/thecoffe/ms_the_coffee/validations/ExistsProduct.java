package com.thecoffe.ms_the_coffee.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ExistsProductValidation.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsProduct {
    String message() default "Producto ya existe.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
