package com.pioner.banking.service.client;

import com.pioner.banking.api.dto.CurrencyRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class ExchangeRateClient {
    public BigDecimal getExchangeRate(String currencyCode, String currencyBaseCode, LocalDate date) {
        CurrencyRequest request = new CurrencyRequest(currencyCode, currencyBaseCode, date);

        BigDecimal rate = WebClient.builder()
                .baseUrl("http://currency-service:8080/api/currencies")
                .build()
                .post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BigDecimal.class)
                .block();

        if (rate == null) {
            throw new RuntimeException("Exchange rate didn't found for currency " + currencyCode + " with base currency " + currencyBaseCode + " on " + date);
        }
        return rate;

    }
}
