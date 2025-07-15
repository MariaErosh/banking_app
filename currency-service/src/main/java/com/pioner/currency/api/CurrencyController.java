package com.pioner.currency.api;

import com.pioner.currency.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;


@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @PostMapping
    public ResponseEntity<BigDecimal> getRateByDate(@Valid @RequestBody CurrencyRequest request){
        String base = request.getIsoBasedCurrency();
        if (base == null || base.trim().isEmpty()) {
            base = "EUR";
        }
        BigDecimal rate = currencyService.getRateByDate(request.getIsoCurrency(), request.getDate());
        if (rate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!base.equalsIgnoreCase("EUR")) {
            BigDecimal baseRate = currencyService.getRateByDate(base, request.getDate());
            rate =  rate.divide(baseRate, 6, RoundingMode.HALF_UP);
        } else {
            rate = rate.setScale(6, RoundingMode.HALF_UP);
        }

        return ResponseEntity.ok(rate);
    }

}
