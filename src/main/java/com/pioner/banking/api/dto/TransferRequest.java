package com.pioner.banking.api.dto;

import java.math.BigDecimal;

public class TransferRequest {
    private Long toUserId;
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
