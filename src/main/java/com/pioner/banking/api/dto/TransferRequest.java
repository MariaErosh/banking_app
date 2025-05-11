package com.pioner.banking.api.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TransferRequest {
    @NotNull
    private Long toUserId;
    @NotNull
    private BigDecimal sum;

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
