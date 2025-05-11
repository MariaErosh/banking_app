package com.pioner.banking.service;

import com.pioner.banking.dao.entity.Account;
import com.pioner.banking.dao.repository.AccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;


@Service
public class ScheduledBalanceUpdater {

    private static final Logger log = LoggerFactory.getLogger(ScheduledBalanceUpdater.class);

    private final AccountRepository accountRepository;
    private final TransactionTemplate transactionTemplate;

    public ScheduledBalanceUpdater(AccountRepository accountRepository, PlatformTransactionManager transactionManager) {
        this.accountRepository = accountRepository;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    /* Increase balance by 10%, but no more than 207% of the initial balance */
    @Scheduled(fixedRate = 30_000)
    @Transactional
    public void autoIncreaseBalance() {
        log.info("Scheduled balance update started");

        Pageable pageable = PageRequest.of(0, 50);
        Page<Account> page;
        int updatedAccounts = 0;

        do {
            page = accountRepository.findAccountsForBalanceIncrease(pageable);
            for (Account account : page.getContent()) {

                transactionTemplate.execute(status -> {
                    BigDecimal currentBalance = account.getBalance();
                    BigDecimal initialBalance = account.getInitBalance();
                    BigDecimal maxBalance = initialBalance.multiply(BigDecimal.valueOf(2.07));
                    BigDecimal increased = currentBalance.multiply(BigDecimal.valueOf(1.1));

                    BigDecimal newBalance = increased.compareTo(maxBalance) > 0 ? maxBalance : increased;
                    log.debug("Account ID {}: currentBalance={}, newBalance={}, maxAllowed={}", account.getId(), currentBalance, newBalance, maxBalance);
                    account.setBalance(newBalance);
                    accountRepository.save(account);
                    return null;
                });
                updatedAccounts++;
            }
            pageable = page.nextPageable();
        } while (page.hasNext());
        log.info("Scheduled balance update completed. Accounts updated: {}", updatedAccounts);
    }
}
