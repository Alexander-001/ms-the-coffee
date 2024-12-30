package com.thecoffe.ms_the_coffee.models;

import jakarta.validation.constraints.NotBlank;

public class Password {

    @NotBlank(message = "La contraseña es obligatoria.")
    String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
