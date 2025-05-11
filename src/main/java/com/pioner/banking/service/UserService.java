package com.pioner.banking.service;

import com.pioner.banking.api.security.JwtUtil;
import com.pioner.banking.dao.entity.EmailData;
import com.pioner.banking.dao.entity.PhoneData;
import com.pioner.banking.dao.entity.User;
import com.pioner.banking.dao.repository.EmailDataRepository;
import com.pioner.banking.dao.repository.PhoneDataRepository;
import com.pioner.banking.dao.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       EmailDataRepository emailDataRepository,
                       PhoneDataRepository phoneDataRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailDataRepository = emailDataRepository;
        this.phoneDataRepository = phoneDataRepository;
    }

    public String authenticateByEmail(String email, String password) {
        log.info("Authenticating by email {}", email);
        EmailData emailData = emailDataRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Authentication failed: email not found - {}", email);
                    return new IllegalArgumentException("Invalid email");
                });

        User user = emailData.getUser();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Authentication failed: invalid password for user id - {}", user.getId());
            throw new IllegalArgumentException("Invalid password");
        }
        log.info("Authentication successful by email for user id -{}", user.getId());
        return jwtUtil.generateToken(user.getId());
    }

    public String authenticateByPhone(String phone, String password) {
        log.info("Authenticating by phone {}", phone);
        PhoneData phoneData = phoneDataRepository.findByPhone(phone)
                .orElseThrow(() -> {
                    log.warn("Authentication failed: email not found - {}", phone);
                    return new IllegalArgumentException("Invalid phone");
                });

        User user = phoneData.getUser();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Authentication failed: invalid password for user id - {}", user.getId());
            throw new IllegalArgumentException("Invalid password");
        }

        log.info("Authentication successful by phone for user id -{}", user.getId());
        return jwtUtil.generateToken(user.getId());
    }

    public User getUserById(Long userId) {
        return userRepository.getReferenceById(userId);
    }


}
