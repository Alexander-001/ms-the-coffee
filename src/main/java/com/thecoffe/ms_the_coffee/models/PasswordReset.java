package com.thecoffe.ms_the_coffee.models;

import jakarta.validation.constraints.NotBlank;

public class PasswordReset {

    @NotBlank(message = "La contraseña actual es requerida.")
    private String currentPassword;
    @NotBlank(message = "La nueva contraseña es requerida.")
    private String newPassword;
    @NotBlank(message = "El email es requerido.")
    private String email;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
