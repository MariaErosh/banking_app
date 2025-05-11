package com.pioner.banking.dao.repository;

import com.pioner.banking.dao.entity.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.user.id = :userId")
    Optional<Account> getAccountForUpdateByUserId(@Param("userId") Long userId);

    @Query("SELECT a FROM Account a WHERE a.balance < a.initBalance * 2.07")
    Page<Account> findAccountsForBalanceIncrease(Pageable pageable);
}
