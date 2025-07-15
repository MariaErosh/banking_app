package com.pioner.banking.api.dto;

import jakarta.validation.constraints.NotEmpty;

public class PhoneUpdateRequest {
    @NotEmpty
    private String phone;
    @NotEmpty
    private String newPhone;

    public String getPhone() {
        return phone;
    }

    public String getNewPhone() {
        return newPhone;
    }
}
