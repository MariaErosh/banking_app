package com.pioner.banking.api.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TransferRequest {
    @NotNull
    private Long toUserId;
    @NotNull
    private BigDecimal sum;

    private String currencyCode;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}
