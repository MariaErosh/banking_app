package com.pioner.banking.dao.repository;

import com.pioner.banking.dao.entity.EmailData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface EmailDataRepository extends JpaRepository<EmailData, Long> {

    boolean existsByEmail(String email);

    Optional<EmailData> findByEmailAndUserId(String email, Long userId);

    Optional<EmailData> findByEmail(String email);

    List<EmailData> findByUserId(Long userId);

    void deleteByEmailAndUserId(String email, Long userId);
}

