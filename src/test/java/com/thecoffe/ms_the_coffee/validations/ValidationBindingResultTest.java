package com.thecoffe.ms_the_coffee.validations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ValidationBindingResultTest {

    @Mock
    private ValidationBindingResult validationBindingResult;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validationBindingResult = new ValidationBindingResult();
    }

    @Test
    void validation() {
        FieldError error1 = new FieldError("user", "email", "Email is required");
        FieldError error2 = new FieldError("user", "password", "Password must be at least 8 characters");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(error1, error2));
        ResponseEntity<Map<String, Object>> response = validationBindingResult.validation(bindingResult);
        assertEquals(400, response.getStatusCodeValue(), "Response status should be 400 BAD REQUEST");
        Map<String, Object> errors = response.getBody();
        assertEquals(2, errors.size(), "There should be two validation errors");
        assertEquals("Email is required", errors.get("email"));
        assertEquals("Password must be at least 8 characters", errors.get("password"));
    }
}