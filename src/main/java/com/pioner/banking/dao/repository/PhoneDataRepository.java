package com.pioner.banking.dao.repository;

import com.pioner.banking.dao.entity.PhoneData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {

    boolean existsByPhone(String phone);

    Optional<PhoneData> findByPhone(String phone);

    List<PhoneData> findByUserId(Long userId);

    Optional<PhoneData> findByPhoneAndUserId(String phone, Long userId);

}
