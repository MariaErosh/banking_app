package com.pioner.banking.api.dto;

import jakarta.validation.constraints.NotEmpty;

public class PhoneAddRequest {
    @NotEmpty
    private String phone;

    public String getPhone() {
        return phone;
    }
}
