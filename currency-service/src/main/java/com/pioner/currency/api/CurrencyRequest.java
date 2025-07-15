package com.pioner.currency.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;


import java.time.LocalDate;

@Data
public class CurrencyRequest {

    @NotNull
    @NotBlank
    private String isoCurrency;

    @Getter
    private String isoBasedCurrency;

    @NotNull
    private LocalDate date;

}
