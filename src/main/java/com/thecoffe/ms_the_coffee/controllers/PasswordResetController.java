package com.thecoffe.ms_the_coffee.controllers;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thecoffe.ms_the_coffee.models.Password;
import com.thecoffe.ms_the_coffee.models.PasswordReset;
import com.thecoffe.ms_the_coffee.models.User;
import com.thecoffe.ms_the_coffee.services.DataStoreService;
import com.thecoffe.ms_the_coffee.services.interfaces.PasswordResetService;
import com.thecoffe.ms_the_coffee.services.interfaces.UserService;
import com.thecoffe.ms_the_coffee.validations.ValidationBindingResult;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/password")
public class PasswordResetController {

    @Autowired
    ValidationBindingResult validationBindingResult;

    @Autowired
    PasswordResetService passwordResetService;

    @Autowired
    UserService userService;

    @Autowired
    private DataStoreService dataStoreService;

    private boolean isValidUUID(String token) {
        try {
            UUID.fromString(token);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @GetMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestParam String token) {
        Optional<PasswordReset> tokenOptional = passwordResetService.findByToken(token);
        Map<String, Object> response = new HashMap<>();
        if (!isValidUUID(token)) {
            response.put("isValid", false);
            response.put("message", "El token no existe o tiene un formato inválido.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (tokenOptional.isPresent()) {
            PasswordReset passwordReset = tokenOptional.get();
            if (passwordReset.getExpirationTime().isAfter(Instant.now())) {
                response.put("isValid", true);
                response.put("message", "Token válido. Puedes cambiar tu contraseña.");
                dataStoreService.save("isValidToken", true);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            passwordResetService.delete(passwordReset.getId());
        }
        response.put("isValid", false);
        response.put("message", "Token inválido o expirado.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updatePassword(@Valid @RequestBody Password password,
            BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        Object isValidToken = dataStoreService.get("isValidToken");
        if ((boolean) isValidToken) {
            if (result.hasFieldErrors()) {
                return validationBindingResult.validation(result);
            }
            User user = (User) dataStoreService.get("user");
            user.setPassword(password.getPassword());
            userService.update(user.getId(), user);
            dataStoreService.delete("isValidToken");
            dataStoreService.delete("user");
            response.put("user", user);
            response.put("message", "Contraseña actualizada correctamente");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("user", null);
        response.put("message", "Token inválido o expirado.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }

}
