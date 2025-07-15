package com.pioner.banking.service;

import com.pioner.banking.dao.entity.Account;
import com.pioner.banking.dao.entity.User;
import com.pioner.banking.dao.repository.AccountRepository;
import com.pioner.banking.dao.repository.UserRepository;
import com.pioner.banking.service.client.ExchangeRateClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TransferServiceTest {

    private AccountRepository accountRepository;
    //private UserRepository userRepository;
    private TransferService transferService;
    private ExchangeRateClient exchangeRateClient;

    private Account accountFrom;
    private Account accountTo;

    @BeforeEach
    void before() {
        accountRepository = mock(AccountRepository.class);
        //userRepository = mock(UserRepository.class);
        //WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        exchangeRateClient = mock(ExchangeRateClient.class);
        transferService = new TransferService(accountRepository, exchangeRateClient);

        User userFrom = new User("Tom", LocalDate.of(1980, 10, 10), "pass1",
                List.of("tom@tom.com"), List.of("123456"));
        userFrom.setId(1L);
        accountFrom = userFrom.getAccount();
        accountFrom.setBalance(new BigDecimal(100));

        User userTo = new User("Jerry", LocalDate.of(1985, 10, 10), "pass2",
                List.of("jerry@tom.com"), List.of("12345"));
        userTo.setId(2L);
        accountTo = userTo.getAccount();
        accountTo.setBalance(new BigDecimal(200));
    }

    @Test
    void testSuccessfulTransfer() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal sum = new BigDecimal(60);

        when(accountRepository.getAccountForUpdateByUserId(fromUserId)).thenReturn(Optional.of(accountFrom));
        when(accountRepository.getAccountForUpdateByUserId(toUserId)).thenReturn(Optional.of(accountTo));

        transferService.transfer(fromUserId, toUserId, sum, "EUR");

        assertEquals(new BigDecimal("40"), accountFrom.getBalance());
        assertEquals(new BigDecimal("260"), accountTo.getBalance());

        verify(accountRepository).save(accountFrom);
        verify(accountRepository).save(accountTo);
    }

    @Test
    void testTransferToSelfThrowsException() {
        Long userId = 1L;

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                transferService.transfer(userId, userId, BigDecimal.TEN, "EUR"));

        assertEquals("Can't transfer to the same user", ex.getMessage());
    }

    @Test
    void testTransferWithNegativeAmountThrowsException() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                transferService.transfer(1L, 2L, new BigDecimal("-100"), "EUR"));

        assertEquals("Incorrect transfer sum", ex.getMessage());
    }

    @Test
    void testInsufficientBalanceThrowsException() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = new BigDecimal("1000");

        when(accountRepository.getAccountForUpdateByUserId(fromUserId)).thenReturn(Optional.of(accountFrom));
        when(accountRepository.getAccountForUpdateByUserId(toUserId)).thenReturn(Optional.of(accountTo));

        Exception ex = assertThrows(IllegalStateException.class, () ->
                transferService.transfer(fromUserId, toUserId, amount, "EUR"));

        assertEquals("Insufficient balance", ex.getMessage());

        verify(accountRepository, never()).save(any());
    }

    @Test
    void testAccountNotFoundThrowsException() {
        when(accountRepository.getAccountForUpdateByUserId(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                transferService.transfer(1L, 2L, BigDecimal.TEN, "EUR"));

        assertEquals("Remitter account not found", ex.getMessage());
    }

    @Test
    void testCurrencyConversionTransfer() {
        accountFrom.setCurrencyCode("USD");
        accountTo.setCurrencyCode("EUR");

        when(accountRepository.getAccountForUpdateByUserId(1L)).thenReturn(Optional.of(accountFrom));
        when(accountRepository.getAccountForUpdateByUserId(2L)).thenReturn(Optional.of(accountTo));

        when(exchangeRateClient.getExchangeRate(eq("USD"), eq("EUR"), any()))
                .thenReturn(BigDecimal.valueOf(0.9));
        when(exchangeRateClient.getExchangeRate(eq("EUR"), eq("USD"), any()))
                .thenReturn(BigDecimal.valueOf(1.2));

        transferService.transfer(1L, 2L, BigDecimal.valueOf(10), "USD");

        assertEquals(BigDecimal.valueOf(90), accountFrom.getBalance());
        assertEquals(BigDecimal.valueOf(200.9), accountTo.getBalance());
    }
}