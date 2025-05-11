package com.pioner.banking.api.dto;

public class EmailAddRequest {
    private String email;

    public EmailAddRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
