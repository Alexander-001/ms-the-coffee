package com.thecoffe.ms_the_coffee.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ExistsCategoryValidation.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsCategory {
    String message() default "La categoría no existe en el sistema.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
