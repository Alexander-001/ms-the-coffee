package com.thecoffe.ms_the_coffee.models;

import jakarta.validation.constraints.NotBlank;

public class UserRole {

    @NotBlank(message = "Nombre de usuario no puede estar vacio.")
    private String username;

    public UserRole() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
