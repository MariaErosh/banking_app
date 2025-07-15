package com.pioner.currency.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Optional<Currency> findByIsoCurrency(String isoCurrency);

    Optional<Currency> findById(Long id);
}
