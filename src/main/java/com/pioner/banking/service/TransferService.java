package com.pioner.banking.service;

import com.pioner.banking.dao.entity.Account;
import com.pioner.banking.dao.repository.AccountRepository;
import com.pioner.banking.dao.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    public TransferService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
    }

    private final AccountRepository accountRepository;

    @Transactional
    public void transfer(Long userIdFrom, Long userIdTo, BigDecimal sum) {
        if (userIdFrom.equals(userIdTo)) {
            throw new IllegalArgumentException("Can't transfer to the same user");
        }

        if (sum == null || sum.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Incorrect transfer sum");
        }

        Account accountFrom, accountTo;

        //prevent deadlock:
        if (userIdFrom < userIdTo) {

            accountFrom = accountRepository.getAccountForUpdateByUserId(userIdFrom)
                    .orElseThrow(() -> new IllegalArgumentException("Remitter account not found"));

            accountTo = accountRepository.getAccountForUpdateByUserId(userIdTo)
                    .orElseThrow(() -> new IllegalArgumentException("Receiver account not found"));
        } else {
            accountTo = accountRepository.getAccountForUpdateByUserId(userIdTo)
                    .orElseThrow(() -> new IllegalArgumentException("Receiver account not found"));

            accountFrom = accountRepository.getAccountForUpdateByUserId(userIdFrom)
                    .orElseThrow(() -> new IllegalArgumentException("Remitter account not found"));

        }
//TODO should add BusinessException:
        if (accountFrom.getBalance().compareTo(sum) < 0)
                throw new IllegalStateException("Insufficient balance");

        accountFrom.setBalance(accountFrom.getBalance().subtract(sum));
        accountTo.setBalance(accountTo.getBalance().add(sum));

        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
    }
}
