package com.pioner.banking.service;


import com.pioner.banking.dao.entity.PhoneData;
import com.pioner.banking.dao.entity.User;
import com.pioner.banking.dao.repository.PhoneDataRepository;
import com.pioner.banking.dao.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhoneService {
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

    @Transactional
    public void addPhone(Long userId, String phone) {
        if (phoneDataRepository.existsByPhone(phone)) {
            throw new IllegalArgumentException("Email is already in use");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PhoneData newPhone = new PhoneData();
        newPhone.setPhone(phone);
        newPhone.setUser(user);

        phoneDataRepository.save(newPhone);
    }

    @Transactional
    public void updatePhone(Long userId, String oldEmail, String newEmail) {
        PhoneData existingPhone = phoneDataRepository.findByPhoneAndUserId(oldEmail, userId)
                .orElseThrow(() -> new IllegalArgumentException("Email not found for the current user"));

        if (phoneDataRepository.existsByPhone(newEmail)) {
            throw new IllegalArgumentException("The new email is already in use");
        }

        existingPhone.setPhone(newEmail);
        phoneDataRepository.save(existingPhone);
    }

    @Transactional
    public void deletePhone(Long userId, String email) {
        PhoneData existingEmail = phoneDataRepository.findByPhoneAndUserId(email, userId)
                .orElseThrow(() -> new IllegalArgumentException("Email not found for the current user"));

        List<PhoneData> allEmails = phoneDataRepository.findByUserId(userId);
        if (allEmails.size() <= 1) {
            throw new IllegalStateException("The user must have at least one email");
        }

        phoneDataRepository.delete(existingEmail);
    }

}
