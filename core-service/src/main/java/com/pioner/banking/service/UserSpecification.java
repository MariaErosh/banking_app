package com.pioner.banking.service;

import com.pioner.banking.dao.entity.EmailData;
import com.pioner.banking.dao.entity.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class UserSpecification {

    public static Specification<User> hasNameLike(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(root.get("name"), name + "%");
    }

    public static Specification<User> hasPhone(String phone) {
        return (root, query, cb) -> {
            if (phone == null) return null;
            Join<User, EmailData> emailJoin = root.join("phones");
            return cb.equal(emailJoin.get("phone"), phone);
        };

    }

    public static Specification<User> hasDateOfBirthAfter(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null : cb.greaterThan(root.get("dateOfBirth"), date);
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, cb) -> {
            if (email == null) return null;
            Join<User, EmailData> emailJoin = root.join("emails");
            return cb.equal(emailJoin.get("email"), email);
        };
    }
}
