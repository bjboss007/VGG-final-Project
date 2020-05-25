package com.vgg.fvp.common.data;

import javax.validation.constraints.NotNull;

public class PasswordDTO {

    @NotNull(message = "Password field must be null")
    private String password;

    public PasswordDTO() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
