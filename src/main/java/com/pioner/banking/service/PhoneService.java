package com.pioner.banking.service;


import com.pioner.banking.dao.entity.PhoneData;
import com.pioner.banking.dao.entity.User;
import com.pioner.banking.dao.repository.PhoneDataRepository;
import com.pioner.banking.dao.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PhoneService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final PhoneDataRepository phoneDataRepository;
    private final UserRepository userRepository;

    public PhoneService(PhoneDataRepository phoneDataRepository, UserRepository userRepository) {
        this.phoneDataRepository = phoneDataRepository;
        this.userRepository = userRepository;
    }

    public List<String> getAllPhones(Long userId) {
        return phoneDataRepository.findByUserId(userId).stream()
                .map(PhoneData::getPhone)
                .collect(Collectors.toList());
    }


    public void addPhone(Long userId, String phone) {
        log.info("Adding phone '{}' to userId {}", phone, userId);

        if (phoneDataRepository.existsByPhone(phone)) {
            log.warn("Phone is already in use");
            throw new IllegalArgumentException("Phone is already in use");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id - {}", userId);
                    return new IllegalArgumentException("User not found");
                });

        PhoneData newPhone = new PhoneData(user, phone);

        phoneDataRepository.save(newPhone);
        log.info("Added successfully");
    }


    public void updatePhone(Long userId, String oldEmail, String newEmail) {
        PhoneData existingPhone = phoneDataRepository.findByPhoneAndUserId(oldEmail, userId)
                .orElseThrow(() -> {
                    log.warn("Old phone not found");
                    return new IllegalArgumentException("Phone not found for the current user");
                });

        if (phoneDataRepository.existsByPhone(newEmail)) {
            log.warn("The new phone is already in use");
            throw new IllegalArgumentException("The new phone is already in use");
        }

        existingPhone.setPhone(newEmail);
        phoneDataRepository.save(existingPhone);
        log.info("Updated successfully");
    }


    public void deletePhone(Long userId, String email) {
        log.info("Deleting phone '{}' for userId {}", email, userId);
        PhoneData existingEmail = phoneDataRepository.findByPhoneAndUserId(email, userId)
                .orElseThrow(() -> {
                    log.warn("Phone not found");
                    return new IllegalArgumentException("Phone not found for the current user");
                });

        List<PhoneData> allEmails = phoneDataRepository.findByUserId(userId);
        if (allEmails.size() <= 1) {
            log.warn("Deletion stopped: user must have at least one phone");
            throw new IllegalStateException("The user must have at least one phone");
        }

        phoneDataRepository.delete(existingEmail);
        log.info("Deleted successfully");
    }

}
