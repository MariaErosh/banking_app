package com.pioner.banking.service;

import com.pioner.banking.api.dto.CurrencyRequest;
import com.pioner.banking.dao.entity.Account;
import com.pioner.banking.dao.repository.AccountRepository;
import com.pioner.banking.dao.repository.UserRepository;
import com.pioner.banking.service.client.ExchangeRateClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class TransferService {

    private static final Logger log = LoggerFactory.getLogger(TransferService.class);

    public TransferService(AccountRepository accountRepository, ExchangeRateClient exchangeRateClient) {
        this.accountRepository = accountRepository;
        this.exchangeRateClient = exchangeRateClient;
    }

    private final AccountRepository accountRepository;

    private final ExchangeRateClient exchangeRateClient;

    @Transactional
    public void transfer(Long userIdFrom, Long userIdTo, BigDecimal sumCurrency, String currencyCode) {
        log.info("Transferring: fromUserId={}, toUserId={}, sum={}, currency={}", userIdFrom, userIdTo, sumCurrency, currencyCode);

        if (currencyCode == null || currencyCode.trim().isEmpty()) {
            currencyCode = "EUR";
            log.warn("Update currency code: EUR");
        }

        if (userIdFrom.equals(userIdTo)) {
            log.warn("Transfer stopped: userIdFrom = userIdTo");
            throw new IllegalArgumentException("Can't transfer to the same user");
        }

        if (sumCurrency == null || sumCurrency.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Transfer stopped: invalid sum");
            throw new IllegalArgumentException("Incorrect transfer sum");
        }

        Account accountFrom, accountTo;

        //prevent deadlock by ordering:
        if (userIdFrom < userIdTo) {

            accountFrom = accountRepository.getAccountForUpdateByUserId(userIdFrom)
                    .orElseThrow(() -> {
                        log.warn("Transfer stopped: Remitter account not found");
                        return new IllegalArgumentException("Remitter account not found");
                    });

            accountTo = accountRepository.getAccountForUpdateByUserId(userIdTo)
                    .orElseThrow(() -> {
                        log.warn("Transfer stopped: Receiver account not found");
                        return new IllegalArgumentException("Receiver account not found");
                    });
        } else {
            accountTo = accountRepository.getAccountForUpdateByUserId(userIdTo)
                    .orElseThrow(() -> {
                        log.warn("Transfer stopped: Receiver account not found");
                        return new IllegalArgumentException("Receiver account not found");
                    });

            accountFrom = accountRepository.getAccountForUpdateByUserId(userIdFrom)
                    .orElseThrow(() -> {
                        log.warn("Transfer stopped: Remitter account not found");
                        return new IllegalArgumentException("Remitter account not found");
                    });

        }

        BigDecimal balanceFrom = accountFrom.getBalance();
        LocalDate date = LocalDate.now();
        BigDecimal sumTransferFrom; // Transfer sum in sender's currency
        BigDecimal sumTransferTo;   // Transfer sum in recipient's currency

        if (accountFrom.getCurrencyCode().equalsIgnoreCase(currencyCode)) {
            if (balanceFrom.compareTo(sumCurrency) < 0) {
                log.warn("Transfer stopped: Insufficient balance");
                throw new IllegalStateException("Insufficient balance");
            }
            sumTransferFrom = sumCurrency;
        } else {

            sumTransferFrom = exchangeRateClient.getExchangeRate(currencyCode, accountFrom.getCurrencyCode(), date);
            if (balanceFrom.compareTo(sumTransferFrom) < 0) {
                log.warn("Transfer stopped: Insufficient balance");
                throw new IllegalStateException("Insufficient balance");
            }
        }
        if (accountTo.getCurrencyCode().equalsIgnoreCase(currencyCode)) {
            sumTransferTo = sumCurrency;
        } else {
            sumTransferTo = exchangeRateClient.getExchangeRate(currencyCode, accountTo.getCurrencyCode(), date);
        }
        accountFrom.setBalance(accountFrom.getBalance().subtract(sumTransferFrom));
        accountTo.setBalance(accountTo.getBalance().add(sumTransferTo));

        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
        log.info("Transfer successful");
    }

/*
    private BigDecimal getExchangeRate(String currencyCode, String currencyBaseCode, LocalDate date) {
        CurrencyRequest request = new CurrencyRequest(currencyCode, currencyBaseCode, date);

        BigDecimal rate = WebClient.builder()
                .baseUrl("http://currency-service/api/currencies")
                .build()
                .post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BigDecimal.class)
                .block();

        if (rate == null) {
            log.error("Exchange rate didn't found for currency"+ currencyCode + " with base currency " + currencyBaseCode + " on " + date);
            throw new RuntimeException("Exchange rate didn't found for currency " + currencyCode + " with base currency " + currencyBaseCode + " on " + date);
        }
        return rate;

    }

 */
}
