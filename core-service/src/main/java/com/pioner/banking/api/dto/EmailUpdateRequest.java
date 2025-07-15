package com.pioner.banking.api.dto;

import jakarta.validation.constraints.NotEmpty;

public class EmailUpdateRequest {
    @NotEmpty
    private String email;
    @NotEmpty
    private String newEmail;

    public EmailUpdateRequest(String email, String newEmail) {
        this.email = email;
        this.newEmail = newEmail;
    }

    public String getEmail() {
        return email;
    }

    public String getNewEmail() {
        return newEmail;
    }
    public EmailUpdateRequest(){};

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }
}
