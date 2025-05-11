package com.pioner.banking.service;

import com.pioner.banking.dao.entity.User;
import com.pioner.banking.dao.repository.EmailDataRepository;

import java.util.List;
import java.util.stream.Collectors;

import com.pioner.banking.dao.entity.EmailData;
import com.pioner.banking.dao.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EmailService {

    private final EmailDataRepository emailDataRepository;
    private final UserRepository userRepository;

    public EmailService(EmailDataRepository emailDataRepository, UserRepository userRepository) {
        this.emailDataRepository = emailDataRepository;
        this.userRepository = userRepository;
    }

    public List<String> getAllEmails(Long userId) {
        return emailDataRepository.findByUserId(userId).stream()
                .map(EmailData::getEmail)
                .collect(Collectors.toList());
    }

    public void addEmail(Long userId, String email) {
        if (emailDataRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already in use");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        EmailData newEmail = new EmailData();
        newEmail.setEmail(email);
        newEmail.setUser(user);

        emailDataRepository.save(newEmail);
    }

    public void updateEmail(Long userId, String oldEmail, String newEmail) {
        EmailData existingEmail = emailDataRepository.findByEmailAndUserId(oldEmail, userId)
                .orElseThrow(() -> new IllegalArgumentException("Email not found for the current user"));

        if (emailDataRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("The new email is already in use");
        }

        existingEmail.setEmail(newEmail);
        emailDataRepository.save(existingEmail);
    }

    public void deleteEmail(Long userId, String email) {
        EmailData existingEmail = emailDataRepository.findByEmailAndUserId(email, userId)
                .orElseThrow(() -> new IllegalArgumentException("Email not found for the current user"));

        List<EmailData> allEmails = emailDataRepository.findByUserId(userId);
        if (allEmails.size() <= 1) {
            throw new IllegalStateException("The user must have at least one email");
        }

        emailDataRepository.delete(existingEmail);
    }

}
