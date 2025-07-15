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

    public EmailAddRequest() {}

    public void setEmail(String email) {
        this.email = email;
    }
}
