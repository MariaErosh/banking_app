package com.pioner.banking.service;

import com.pioner.banking.dao.entity.Account;
import com.pioner.banking.dao.repository.AccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ScheduledBalanceUpdater {

    private final AccountRepository accountRepository;

    public ScheduledBalanceUpdater(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /* Increase balance by 10%, but no more than 207% of the initial balance */
    @Scheduled(fixedRate = 30_000)
    @Transactional
    public void autoIncreaseBalance() {

        Pageable pageable = PageRequest.of(0, 50);
        Page<Account> page;

        do {
            page = accountRepository.findAccountsForBalanceIncrease(pageable);
            for (Account account : page.getContent()) {
                BigDecimal currentBalance = account.getBalance();
                BigDecimal initialBalance = account.getInitBalance();

                BigDecimal maxBalance = initialBalance.multiply(BigDecimal.valueOf(2.07));
                BigDecimal increased = currentBalance.multiply(BigDecimal.valueOf(1.1));

                BigDecimal newBalance = increased.compareTo(maxBalance) > 0 ? maxBalance : increased;

                account.setBalance(newBalance);
            }

            accountRepository.saveAll(page.getContent());

            pageable = page.nextPageable();
        } while (page.hasNext());
    }
}
