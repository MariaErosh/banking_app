package com.pioner.banking.service;

import com.pioner.banking.dao.entity.Account;
import com.pioner.banking.dao.repository.AccountRepository;
import com.pioner.banking.dao.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    private static final Logger log = LoggerFactory.getLogger(TransferService.class);

    public TransferService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
    }

    private final AccountRepository accountRepository;

    @Transactional
    public void transfer(Long userIdFrom, Long userIdTo, BigDecimal sum) {
        log.info("Transferring: fromUserId={}, toUserId={}, sum={}", userIdFrom, userIdTo, sum);

        if (userIdFrom.equals(userIdTo)) {
            log.warn("Transfer stopped: userIdFrom = userIdTo");
            throw new IllegalArgumentException("Can't transfer to the same user");
        }

        if (sum == null || sum.compareTo(BigDecimal.ZERO) <= 0) {
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
        if (accountFrom.getBalance().compareTo(sum) < 0) {
            log.warn("Transfer stopped: Insufficient balance");
            throw new IllegalStateException("Insufficient balance");
        }

        accountFrom.setBalance(accountFrom.getBalance().subtract(sum));
        accountTo.setBalance(accountTo.getBalance().add(sum));

        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
        log.info("Transfer successful");
    }
}
