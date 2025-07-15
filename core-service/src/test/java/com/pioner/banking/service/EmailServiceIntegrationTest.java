package com.pioner.banking.service;

import com.pioner.banking.TestContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(initializers = TestContainerConfig.Initializer.class)
class EmailServiceIntegrationTest {

    @Autowired
    private EmailService emailService;

    @Test
    void shouldAddNewEmail() {
        // given
        String email = "test@test.com";

        // when
        emailService.addEmail(1L, email);

        // then
        List<String> emails = emailService.getAllEmails(1L);
        assertTrue(emails.contains(email));
    }

}