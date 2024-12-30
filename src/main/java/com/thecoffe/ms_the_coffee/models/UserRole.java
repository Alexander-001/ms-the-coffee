package com.thecoffe.ms_the_coffee.models;

import jakarta.validation.constraints.NotBlank;

public class UserRole {

    @NotBlank(message = "Email no puede estar vacio.")
    private String email;

    public UserRole() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
