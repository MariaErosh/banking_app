package com.pioner.currency.service;

import com.pioner.currency.dao.Currency;
import com.pioner.currency.dao.CurrencyRepository;
import com.pioner.currency.dao.ExchangeRate;
import com.pioner.currency.dao.ExchangeRateRepository;
import com.pioner.currency.dto.ApiECBResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final WebClient.Builder webСlientBuilder;

    public BigDecimal getRateByDate(String isoCurrency, LocalDate date) {
        Currency curr = currencyRepository.findByIsoCurrency(isoCurrency).orElseGet(() -> fetchAndSaveCurrency(isoCurrency));
        return exchangeRateRepository
                .findByCurrencyAndDate(curr, date)
                .map(ExchangeRate::getRate)
                .orElseGet(()-> fetchAndSaveExchangeRate(curr, date));
    }

    private Currency fetchAndSaveCurrency(String isoCurrency) {
        Currency currency = new Currency();
        currency.setIsoCurrency(isoCurrency);
        currencyRepository.save(currency);
        return currency;
    }

    private BigDecimal fetchAndSaveExchangeRate(Currency currency, LocalDate date) {
        /* curl -s https://api.frankfurter.dev/v1/1999-01-04?base=USD&symbols=EUR */
        String isoCode = currency.getIsoCurrency();
        ApiECBResponse response = WebClient.builder()
                .baseUrl("https://api.frankfurter.dev/v1/")
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("{dateString}")
                        .queryParam("base", "EUR")
                        .queryParam("symbols", isoCode)
                        .build(date.toString())
                )
                .retrieve()
                .bodyToMono(ApiECBResponse.class)
                .block();

        if (response == null || response.getRates() == null || !response.getRates().containsKey(isoCode))
            throw new RuntimeException("Exchange rate didn't found for currency " + isoCode + " on " + date);

        //save to repository:
        currencyRepository.save(currency);
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setCurrency(currency);
        BigDecimal rate = BigDecimal.valueOf(response.getRates().get(isoCode)) .setScale(4, RoundingMode.HALF_UP);
        exchangeRate.setRate(rate);
        exchangeRate.setDate(date);
        exchangeRateRepository.save(exchangeRate);
        return rate;

    }





}
