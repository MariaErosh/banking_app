package com.pioner.banking.service;

import com.pioner.banking.dao.entity.User;
import com.pioner.banking.dao.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserSearchService {

    private final UserRepository userRepository;

    public UserSearchService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<User> searchUsers(String name, String phone, String email, LocalDate dateOfBirthAfter, Pageable pageable) {
        Specification<User> spec = Specification
                .where(UserSpecification.hasNameLike(name))
                .and(UserSpecification.hasPhone(phone))
                .and(UserSpecification.hasEmail(email))
                .and(UserSpecification.hasDateOfBirthAfter(dateOfBirthAfter));

        return userRepository.findAll(spec, pageable);
    }
}
