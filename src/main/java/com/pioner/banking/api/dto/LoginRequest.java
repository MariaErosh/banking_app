package com.pioner.banking.api.dto;

import jakarta.validation.constraints.NotEmpty;

public class LoginRequest {
    @NotEmpty
    private String login;
    @NotEmpty
    private String password;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
