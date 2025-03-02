package com.thecoffe.ms_the_coffee.controllers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
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
import com.thecoffe.ms_the_coffee.services.DataStoreService;
import com.thecoffe.ms_the_coffee.services.EmailService;
import com.thecoffe.ms_the_coffee.services.interfaces.PasswordEmailResetService;
import com.thecoffe.ms_the_coffee.services.interfaces.UserService;
import com.thecoffe.ms_the_coffee.validations.ValidationBindingResult;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEmailResetService passwordEmailResetService;

    @Autowired
    private ValidationBindingResult validationBindingResult;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DataStoreService dataStoreService;

    // * Get all users
    @GetMapping
    public ResponseEntity<Map<String, Object>> findAllUsers() {
        List<User> users = userService.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuarios encontradas");
        response.put("users", users);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // * Get all users
    @PostMapping("/info")
    public ResponseEntity<Map<String, Object>> findUserByEmail(@Valid @RequestBody UserRole userRole,
            BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationBindingResult.validation(result);
        }
        Optional<User> user = userService.findByEmail(userRole.getEmail());
        Map<String, Object> response = new HashMap<>();
        if (user.isPresent()) {
            response.put("message", "Usuario encontrado");
            response.put("user", user.orElseThrow());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        response.put("message", "No se encontro usuario");
        response.put("user", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@Valid @RequestBody UserRole userRole,
            BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationBindingResult.validation(result);
        }
        Optional<User> userOptional = userService.findByEmail(userRole.getEmail());
        Map<String, Object> response = new HashMap<>();
        if (userOptional.isPresent()) {
            User user = userOptional.orElseThrow();
            String token = UUID.randomUUID().toString();
            Instant expirationTime = Instant.now().plus(1, ChronoUnit.HOURS);
            passwordEmailResetService.save(user.getId(), token, expirationTime);
            String resetUrl = String.format("http://localhost:3000/login/reset-password?token=%s", token);
            Model model = new ExtendedModelMap();
            model.addAttribute("name", user.getFirstName() + " " + user.getLastName());
            model.addAttribute("message",
                    "Lamentamos que tengas problemas para iniciar sesión en The Company. Hemos recibido un mensaje conforme has olvidado tu contraseña, si has sido tu puedes cambiar la contraseña ahora.");
            model.addAttribute("resetUrl", resetUrl);
            emailService.sendEmail(user.getEmail(), "Recuperación de cuenta",
                    "forgot-password", model);
            response.put("message",
                    "Se ha enviado un correo electronico con un enlace para volver a ingresar tu cuenta.");
            response.put("user", user);
            dataStoreService.save("user", user);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("user", null);
        response.put("message", "Usuario no existe.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    /*
     * public ResponseEntity<Map<String, Object>> updatePassword(@Valid @RequestBody
     * PasswordReset passwordReset){
     * 
     * }
     */

    // * Register new user with ROLE_ADMIN
    @PostMapping
    public ResponseEntity<Map<String, Object>> registerUserAdmin(@Valid @RequestBody User user, BindingResult result) {
        user.setAdmin(true);
        return registerNewUser(user, result);
    }

    // * Register new user with ROLE_USER
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody User user, BindingResult result) {
        user.setAdmin(false);
        return registerNewUser(user, result);
    }

    // * Method to register a new user, used in: registerAdmin, registerUser.
    private ResponseEntity<Map<String, Object>> registerNewUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationBindingResult.validation(result);
        }
        Map<String, Object> response = new HashMap<>();
        boolean emailExists = userService.existsByEmail(user.getEmail());
        if (emailExists) {
            response.put("message", "El usuario con email: " + user.getEmail() + " ya existe");
            response.put("user", null);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        boolean rutExists = userService.existsByRut(user.getRut());
        if (rutExists) {
            response.put("message", "El usuario con rut: " + user.getRut() + " ya existe");
            response.put("user", null);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        User newUser = new User();
        newUser.setRut(user.getRut());
        newUser.setEmail(user.getEmail());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPhone(user.getPhone());
        newUser.setGender(user.getGender());
        newUser.setBirthDate(user.getBirthDate());
        newUser.setCountry(user.getCountry());
        newUser.setCity(user.getCity());
        newUser.setAddress(user.getAddress());
        newUser.setImage(user.getImage());
        newUser.setPassword(user.getPassword());
        newUser.setAdmin(user.isAdmin());
        newUser.setPosition(user.getPosition());
        newUser.setTeam(user.getTeam());
        try {
            User savedUser = userService.save(newUser);
            Model model = new ExtendedModelMap();
            model.addAttribute("name", user.getFirstName() + " " + user.getLastName());
            model.addAttribute("message",
                    "Gracias por unirte a nuestro equipo, hemos creado una contraseña provisional para que puedas iniciar sesión.");
            model.addAttribute("email", user.getEmail());
            model.addAttribute("password", user.getPassword());
            emailService.sendEmail(user.getEmail(), "Bienvenido a the company",
                    "email", model);
            response.put("message", "Usuario creado correctamente");
            response.put("user", savedUser);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            response.put("message", "Error al crear el usuario");
            response.put("user", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // * Update user by id
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@Valid @RequestBody User user, BindingResult result,
            @PathVariable Long id) {
        if (result.hasFieldErrors()) {
            return validationBindingResult.validation(result);
        }
        Optional<User> updateUser = userService.update(id, user);
        Map<String, Object> response = new HashMap<>();
        if (updateUser.isPresent()) {
            user.setId(id);
            Model model = new ExtendedModelMap();
            model.addAttribute("name", user.getFirstName() + " " + user.getLastName());
            model.addAttribute("message",
                    "Te comentamos que tu usuario ha sido actualizado.");
            model.addAttribute("email", user.getEmail());
            model.addAttribute("password", user.getPassword());
            emailService.sendEmail(user.getEmail(), "Actualización de usuario",
                    "email", model);
            response.put("message", "Usuario actualizado");
            response.put("user", updateUser.orElseThrow());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("message", "No se pudo actualizar usuario");
        response.put("user", null);
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
        response.put("user", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // * Delete ROLE_ADMIN
    @PutMapping("/role-user")
    public ResponseEntity<Map<String, Object>> deleteAdminRole(@Valid @RequestBody UserRole userRole,
            BindingResult result) {
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
        response.put("user", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // * Delete user by id
    // !failed when delete, is deleted but return 403
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        Optional<User> userDelete = userService.delete(id);
        Map<String, Object> response = new HashMap<>();
        if (userDelete.isPresent()) {
            response.put("message", "Dirección eliminada");
            response.put("user", userDelete.orElseThrow());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("message", "No se pudo eliminar dirección");
        response.put("user", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
