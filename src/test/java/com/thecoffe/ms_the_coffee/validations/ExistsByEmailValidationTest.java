package com.thecoffe.ms_the_coffee.validations;

import com.thecoffe.ms_the_coffee.services.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ExistsByEmailValidationTest {

    @InjectMocks
    private ExistsByEmailValidation existsByEmailValidation;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isValidEmailExists() {
        when(userService.existsByEmail("test@thecoffe.com")).thenReturn(true);
        boolean isValid = existsByEmailValidation.isValid("test@thecoffe.com", null);
        assertFalse(isValid, "Validacion se cae cuando el correo existe");
    }

    @Test
    void isValidEmailNotExists() {
        when(userService.existsByEmail("new@thecoffe.com")).thenReturn(false);
        boolean isValid = existsByEmailValidation.isValid("new@thecoffe.com", null);
        assertTrue(isValid, "Validacion se cae cuando el correo no existe");
    }

    @Test
    void isValidUsernameNull() {
        existsByEmailValidation = new ExistsByEmailValidation();
        boolean isValid = existsByEmailValidation.isValid("test@thecoffe.com", null);
        assertTrue(isValid, "Validacion se cae cuando el usuario es null");
    }
}