package com.thecoffe.ms_the_coffee.controllers;

import com.thecoffe.ms_the_coffee.models.PasswordReset;
import com.thecoffe.ms_the_coffee.models.User;
import com.thecoffe.ms_the_coffee.models.UserRole;
import com.thecoffe.ms_the_coffee.services.DataStoreService;
import com.thecoffe.ms_the_coffee.services.EmailService;
import com.thecoffe.ms_the_coffee.services.impl.UserServiceImpl;
import com.thecoffe.ms_the_coffee.services.interfaces.PasswordResetService;
import com.thecoffe.ms_the_coffee.validations.ValidationBindingResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private PasswordResetService passwordResetService;

    @Mock
    private EmailService emailService;

    @Mock
    private DataStoreService dataStoreService;

    @Mock
    private ValidationBindingResult validationBindingResult;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UserController userController;

    private User user;

    private UserRole userRole;

    private PasswordReset passwordReset;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

        userRole = new UserRole();
        userRole.setEmail("email1@gmail.com");

        validationBindingResult = new ValidationBindingResult();

        passwordReset = new PasswordReset();
        passwordReset.setId(1L);
        passwordReset.setToken("token1");
        passwordReset.setUserId(1L);
        passwordReset.setExpirationTime(Instant.now().plus(1, ChronoUnit.HOURS));

    }

    @Test
    void findAllUsers() {
        when(userService.findAll()).thenReturn(Collections.singletonList(user));
        ResponseEntity<Map<String, Object>> response = userController.findAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void findUserByEmailErrors() {
        isErrorFields();
        userController.findUserByEmail(userRole, bindingResult);
    }

    @Test
    void findUserByEmailUserFound() {
        when(userService.findByEmail(userRole.getEmail())).thenReturn(Optional.of(user));
        ResponseEntity<Map<String, Object>> response = userController.findUserByEmail(userRole, mock(BindingResult.class));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertEquals("Usuario encontrado", responseBody.get("message"));
        assertEquals(user, responseBody.get("user"));
    }

    @Test
    void findUserByEmailUserNotFound() {
        when(userService.findByEmail(userRole.getEmail())).thenReturn(Optional.empty());
        ResponseEntity<Map<String, Object>> response = userController.findUserByEmail(userRole, mock(BindingResult.class));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertEquals("No se encontro usuario", responseBody.get("message"));
        assertNull(responseBody.get("user"));
    }

    @Test
    void forgotPasswordErrors() {
        isErrorFields();
        userController.forgotPassword(userRole, bindingResult);
    }

    @Test
    void forgotPasswordUserNotFound() {
        when(userService.findByEmail(userRole.getEmail())).thenReturn(Optional.empty());
        ResponseEntity<Map<String, Object>> response = userController.forgotPassword(userRole, mock(BindingResult.class));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void forgotPasswordUserFound() {
        Model model = new ExtendedModelMap();
        when(userService.findByEmail(userRole.getEmail())).thenReturn(Optional.of(user));
        when(passwordResetService.save(1L, UUID.randomUUID().toString(), Instant.now().plus(1, ChronoUnit.HOURS))).thenReturn(passwordReset);
        doNothing().when(emailService).sendEmail("email@correo.com", "Recuperación de cuenta", "forgot-password", model);
        doNothing().when(dataStoreService).save("user", user);
        ResponseEntity<Map<String, Object>> response = userController.forgotPassword(userRole, mock(BindingResult.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void registerUserAdmin() {
        isErrorFields();
        ResponseEntity<Map<String, Object>> response = userController.registerUserAdmin(user, bindingResult);
        assertNull(response);
    }

    @Test
    void registerUser() {
        isErrorFields();
        ResponseEntity<Map<String, Object>> response = userController.registerUser(user, bindingResult);
        assertNull(response);
    }

    @Test
    void userExists() {
        when(userService.existsByEmail(user.getEmail())).thenReturn(true);
        ResponseEntity<Map<String, Object>> response = userController.registerUserAdmin(user, bindingResult);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void rutExists() {
        when(userService.existsByEmail(user.getEmail())).thenReturn(false);
        when(userService.existsByRut(user.getRut())).thenReturn(true);
        ResponseEntity<Map<String, Object>> response = userController.registerUserAdmin(user, bindingResult);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void registerNewUser() {
        Model model = new ExtendedModelMap();
        when(userService.existsByEmail(user.getEmail())).thenReturn(false);
        when(userService.existsByRut(user.getRut())).thenReturn(false);
        when(userService.save(user)).thenReturn(user);
        doNothing().when(emailService).sendEmail("email@correo.com", "Recuperación de cuenta", "forgot-password", model);
        ResponseEntity<Map<String, Object>> response = userController.registerUserAdmin(user, bindingResult);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void registerNewUser_whenExceptionThrown() {
        when(userService.existsByEmail(user.getEmail())).thenReturn(false);
        when(userService.existsByRut(user.getRut())).thenReturn(false);
        doThrow(new RuntimeException("Database error")).when(userService).save(any(User.class));
        ResponseEntity<Map<String, Object>> response = userController.registerUserAdmin(user, bindingResult);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    void updateUserErrors() {
        isErrorFields();
        ResponseEntity<Map<String, Object>> response = userController.updateUser(user, bindingResult, 1L);
        assertNull(response);
    }

    @Test
    void updateUser() {
        Model model = new ExtendedModelMap();
        when(userService.update(1L, user)).thenReturn(Optional.of(user));
        doNothing().when(emailService).sendEmail("email@correo.com", "Recuperación de cuenta", "forgot-password", model);
        ResponseEntity<Map<String, Object>> response = userController.updateUser(user, bindingResult, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void notUpdateUser() {
        when(userService.update(1L, user)).thenReturn(Optional.empty());
        ResponseEntity<Map<String, Object>> response = userController.updateUser(user, bindingResult, 1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void addAdminRoleErrors() {
        isErrorFields();
        ResponseEntity<Map<String, Object>> response = userController.addAdminRole(userRole, bindingResult);
        assertNull(response);
    }
    @Test
    void notAddAdminRole() {
        when(userService.updateRoleAdmin(userRole)).thenReturn(Optional.empty());
        ResponseEntity<Map<String, Object>> response = userController.addAdminRole(userRole, bindingResult);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void addAdminRole() {
        when(userService.updateRoleAdmin(userRole)).thenReturn(Optional.of(user));
        ResponseEntity<Map<String, Object>> response = userController.addAdminRole(userRole, bindingResult);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void deleteAdminRoleErrors() {
        isErrorFields();
        ResponseEntity<Map<String, Object>> response = userController.deleteAdminRole(userRole, bindingResult);
        assertNull(response);
    }

    @Test
    void notDeleteAdminRole() {
        when(userService.updateRoleUser(userRole)).thenReturn(Optional.empty());
        ResponseEntity<Map<String, Object>> response = userController.deleteAdminRole(userRole, bindingResult);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteAdminRole() {
        when(userService.updateRoleUser(userRole)).thenReturn(Optional.of(user));
        ResponseEntity<Map<String, Object>> response = userController.deleteAdminRole(userRole, bindingResult);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void notDeleteUser() {
        when(userService.delete(1L)).thenReturn(Optional.empty());
        ResponseEntity<Map<String, Object>> response = userController.deleteUser(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteUser() {
        when(userService.delete(1L)).thenReturn(Optional.of(user));
        ResponseEntity<Map<String, Object>> response = userController.deleteUser(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    void isErrorFields() {
        when(bindingResult.hasFieldErrors()).thenReturn(true);
        when(validationBindingResult.validation(bindingResult)).thenReturn(null);
    }
}