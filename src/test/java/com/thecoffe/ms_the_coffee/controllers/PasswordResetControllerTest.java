package com.thecoffe.ms_the_coffee.controllers;

import com.thecoffe.ms_the_coffee.models.Password;
import com.thecoffe.ms_the_coffee.models.PasswordReset;
import com.thecoffe.ms_the_coffee.models.User;
import com.thecoffe.ms_the_coffee.services.DataStoreService;
import com.thecoffe.ms_the_coffee.services.interfaces.PasswordResetService;
import com.thecoffe.ms_the_coffee.services.interfaces.UserService;
import com.thecoffe.ms_the_coffee.validations.ValidationBindingResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class PasswordResetControllerTest {

    @Mock
    private PasswordResetService passwordResetService;

    @Mock
    private DataStoreService dataStoreService;

    @Mock
    private UserService userService;

    @Mock
    private ValidationBindingResult validationBindingResult;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private PasswordResetController passwordResetController;

    private PasswordReset passwordReset;

    private Password password;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordReset = new PasswordReset();
        passwordReset.setId(1L);
        passwordReset.setToken("token");
        passwordReset.setUserId(2L);
        passwordReset.setExpirationTime(Instant.now().plusSeconds(10));
        password = new Password();
        password.setPassword("password");
        user = new User();
        user.setId(1L);
        user.setRut("rut1");
        user.setEmail("email1@gmail.com");
        user.setFirstName("first1");
        user.setLastName("last1");
        user.setPhone("phone1");
        user.setGender("male");
        user.setBirthDate("11/11/1111");
        user.setCountry("country1");
        user.setCity("city1");
        user.setAddress("address1");
        user.setPassword("password1");
        user.setPosition("position1");
        user.setTeam("team1");
        user.setImage("image1");
        user.setAdmin(true);
    }

    @Test
    void resetPasswordInvalidToken() {
        when(passwordResetService.findByToken("token")).thenReturn(Optional.of(passwordReset));
        ResponseEntity<Map<String, Object>> response = passwordResetController.resetPassword("token");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void resetPasswordValidTokenAndEmpty() {
        when(passwordResetService.findByToken("token")).thenReturn(Optional.empty());
        ResponseEntity<Map<String, Object>> response = passwordResetController.resetPassword("7fd48326-4613-4c4a-9ff9-1f2fa981ed6a");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void resetPasswordValidToken() {
        when(passwordResetService.findByToken("7fd48326-4613-4c4a-9ff9-1f2fa981ed6a")).thenReturn(Optional.of(passwordReset));
        doNothing().when(dataStoreService).save("token", true);
        ResponseEntity<Map<String, Object>> response = passwordResetController.resetPassword("7fd48326-4613-4c4a-9ff9-1f2fa981ed6a");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void notUpdatePasswordErrors() {
        isErrorFields();
        when(dataStoreService.get("isValidToken")).thenReturn(true);
        ResponseEntity<Map<String, Object>> response = passwordResetController.updatePassword(password, bindingResult);
        assertNull(response);
    }

    @Test
    void notUpdatePassword() {
        when(dataStoreService.get("isValidToken")).thenReturn(false);
        ResponseEntity<Map<String, Object>> response = passwordResetController.updatePassword(password, bindingResult);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updatePassword() {
        when(dataStoreService.get("isValidToken")).thenReturn(true);
        when(userService.update(1L, user)).thenReturn(Optional.of(user));
        when(dataStoreService.get("user")).thenReturn(user);
        doNothing().when(dataStoreService).delete("isValidToken");
        ResponseEntity<Map<String, Object>> response = passwordResetController.updatePassword(password, bindingResult);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    void isErrorFields() {
        when(bindingResult.hasFieldErrors()).thenReturn(true);
        when(validationBindingResult.validation(bindingResult)).thenReturn(null);
    }

}