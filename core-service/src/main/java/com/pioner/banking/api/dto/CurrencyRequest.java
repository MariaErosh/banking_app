package com.pioner.banking.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CurrencyRequest {
    private String currencyCode;

    private String currencyBaseCode;

    private LocalDate date;

    public CurrencyRequest(String currencyCode, String currencyBaseCode, LocalDate date) {
        this.currencyCode = currencyCode;
        this.currencyBaseCode = currencyBaseCode;
        this.date = date;
    }

    public CurrencyRequest() {}

    public String getCurrencyBaseCode() {
        return currencyBaseCode;
    }

    public void setCurrencyBaseCode(String currencyBaseCode) {
        this.currencyBaseCode = currencyBaseCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
