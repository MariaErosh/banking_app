package com.pioner.banking.api.dto;

import jakarta.validation.constraints.NotEmpty;

public class EmailAddRequest {
    @NotEmpty
    private String email;

    public EmailAddRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
