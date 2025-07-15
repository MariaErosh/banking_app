package com.pioner.banking.service;

import com.pioner.banking.dao.entity.User;
import com.pioner.banking.dao.repository.EmailDataRepository;

import java.util.List;
import java.util.stream.Collectors;

import com.pioner.banking.dao.entity.EmailData;
import com.pioner.banking.dao.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

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
        log.info("Adding email '{}' to userId {}", email, userId);

        if (emailDataRepository.existsByEmail(email)) {
            log.warn("Email is already in use");
            throw new IllegalArgumentException("Email is already in use");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id - {}", userId);
                   return new IllegalArgumentException("User not found");
                });

        EmailData newEmail = new EmailData(user, email);

        emailDataRepository.save(newEmail);
        log.info("Added successfully");
    }

    public void updateEmail(Long userId, String oldEmail, String newEmail) {
        log.info("Updating email from '{}' to '{}' for userId {}", oldEmail, newEmail, userId);
        EmailData existingEmail = emailDataRepository.findByEmailAndUserId(oldEmail, userId)
                .orElseThrow(() -> {
                    log.warn("Old email not found");
                    return new IllegalArgumentException("Email not found for the current user");
                });

        if (emailDataRepository.existsByEmail(newEmail)) {
            log.warn("The new email is already in use");
            throw new IllegalArgumentException("The new email is already in use");
        }

        existingEmail.setEmail(newEmail);
        emailDataRepository.save(existingEmail);
        log.info("Updated successfully");
    }

    public void deleteEmail(Long userId, String email) {
        log.info("Deleting email '{}' for userId {}", email, userId);
        EmailData existingEmail = emailDataRepository.findByEmailAndUserId(email, userId)
                .orElseThrow(() -> {
                    log.warn("Email not found");
                    return new IllegalArgumentException("Email not found for the current user");
                });

        List<EmailData> allEmails = emailDataRepository.findByUserId(userId);
        if (allEmails.size() <= 1) {
            log.warn("Deletion stopped: user must have at least one email");
            throw new IllegalStateException("The user must have at least one email");
        }

        emailDataRepository.delete(existingEmail);
        log.info("Deleted successfully");
    }

}
