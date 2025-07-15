package com.pioner.currency;

import com.pioner.currency.dao.Currency;
import com.pioner.currency.dao.CurrencyRepository;
import com.pioner.currency.dao.ExchangeRate;
import com.pioner.currency.dao.ExchangeRateRepository;
import com.pioner.currency.service.CurrencyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;


@SpringBootTest
@Testcontainers
public class CurrencyIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private CurrencyRepository repository;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private CurrencyService currencyService;

    @Test
    void shouldReturnCorrectRateFromPostgres() {
        Currency currency = new Currency();
        currency.setIsoCurrency("USD");
        repository.save(currency);
        ExchangeRate rate = new  ExchangeRate(null, currency, LocalDate.of(2025, 7, 6), BigDecimal.valueOf(1.11));
        exchangeRateRepository.save(rate);

        BigDecimal actual = currencyService.getRateByDate("USD", LocalDate.of(2025, 7, 6));
        Assertions.assertEquals(0, actual.compareTo(BigDecimal.valueOf(1.11)));
    }

}
