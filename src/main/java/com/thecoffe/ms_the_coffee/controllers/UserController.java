package com.thecoffe.ms_the_coffee.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thecoffe.ms_the_coffee.models.User;
import com.thecoffe.ms_the_coffee.models.UserRole;
import com.thecoffe.ms_the_coffee.services.UserService;
import com.thecoffe.ms_the_coffee.validations.ValidationBindingResult;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ValidationBindingResult validationBindingResult;

    // * Get all users
    @GetMapping
    public ResponseEntity<Map<String, Object>> findAllUsers() {
        List<User> users = userService.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuarios encontradas");
        response.put("users", users);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // * Get user by id
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> findUserById(@PathVariable Long id) {
        Optional<User> userDb = userService.findById(id);
        Map<String, Object> response = new HashMap<>();
        if (userDb.isPresent()) {
            response.put("message", "Usuario encontrado");
            response.put("user", userDb.orElseThrow());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("message", "No existe el usuario");
        response.put("user", new HashMap<>());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // * Register new user with ROLE_ADMIN
    @PostMapping
    public ResponseEntity<Map<String, Object>> registerUserAdmin(@Valid @RequestBody User user, BindingResult result) {
        user.setAdmin(true);
        return create(user, result);
    }

    // * Register new user with ROLE_USER
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody User user, BindingResult result) {
        user.setAdmin(false);
        return create(user, result);
    }

    // * Method to register a new user, used in: registerAdmin, registerUser.
    private ResponseEntity<Map<String, Object>> create(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationBindingResult.validation(result);
        }
        User newUser = userService.save(user);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuario creado correctamente");
        response.put("user", newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // * Update user by id
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@Valid @RequestBody User user, BindingResult result,
            @PathVariable Long id) {
        if (result.hasFieldErrors()) {
            return validationBindingResult.validation(result);
        }
        Optional<User> updateUser = userService.update(id, user);
        Map<String, Object> response = new HashMap<>();
        if (updateUser.isPresent()) {
            user.setId(id);
            response.put("message", "Usuario actualizado");
            response.put("user", updateUser.orElseThrow());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("message", "No se pudo actualizar usuario");
        response.put("user", new HashMap<>());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // * Update user role with ROLE_ADMIN
    @PutMapping("/role-admin")
    public ResponseEntity<Map<String, Object>> addAdminRole(@Valid @RequestBody UserRole userRole,
            BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationBindingResult.validation(result);
        }
        Optional<User> updateRole = userService.updateRoleAdmin(userRole);
        Map<String, Object> response = new HashMap<>();
        if (updateRole.isPresent()) {
            response.put("message", "Role Admin agregado a usuario");
            response.put("user", updateRole.orElseThrow());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        response.put("message", "No se pudo agregar role Admin");
        response.put("user", new HashMap<>());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // * Delete ROLE_ADMIN
    @PutMapping("/role-user")
    public ResponseEntity<?> deleteAdminRole(@Valid @RequestBody UserRole userRole, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationBindingResult.validation(result);
        }
        Optional<User> updateRole = userService.updateRoleUser(userRole);
        Map<String, Object> response = new HashMap<>();
        if (updateRole.isPresent()) {
            response.put("message", "Role Admin eliminado a usuario");
            response.put("user", updateRole.orElseThrow());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        response.put("message", "No se pudo eliminar role Admin");
        response.put("user", new HashMap<>());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // * Delete user by id
    // !failed when delete, is deleted but return 403
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> userDelete = userService.delete(id);
        Map<String, Object> response = new HashMap<>();
        if (userDelete.isPresent()) {
            response.put("message", "Dirección eliminada");
            response.put("user", userDelete.orElseThrow());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("message", "No se pudo eliminar dirección");
        response.put("user", new HashMap<>());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
