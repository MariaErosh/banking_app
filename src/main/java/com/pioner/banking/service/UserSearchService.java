package com.pioner.banking.service;

import com.pioner.banking.dao.entity.User;
import com.pioner.banking.dao.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;

@Service
public class UserSearchService {

    private static final Logger log = LoggerFactory.getLogger(UserSearchService.class);

    private final UserRepository userRepository;

    public UserSearchService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<User> searchUsers(String name, String phone, String email, LocalDate dateOfBirthAfter, Pageable pageable) {
        log.info("Search user with params: name='{}', phone='{}', email='{}', dateOfBirthAfter='{}', page={}",
                name, phone, email, dateOfBirthAfter, pageable);

        Specification<User> spec = Specification
                .where(UserSpecification.hasNameLike(name))
                .and(UserSpecification.hasPhone(phone))
                .and(UserSpecification.hasEmail(email))
                .and(UserSpecification.hasDateOfBirthAfter(dateOfBirthAfter));

        Page<User> result = userRepository.findAll(spec, pageable);;
        log.info("User search completed. Found {} users.", result.getTotalElements());

        return result;
    }
}
