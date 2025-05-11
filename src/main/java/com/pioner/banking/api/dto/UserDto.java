package com.pioner.banking.api.dto;

import com.pioner.banking.dao.entity.EmailData;
import com.pioner.banking.dao.entity.PhoneData;
import com.pioner.banking.dao.entity.User;

import java.math.BigDecimal;
import java.util.List;

public class UserDto {
    private final Long id;

    private final String name;

    private final List<String> emails;

    private final List<String> phones;

    private final BigDecimal balance;


    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.emails = user.getEmails().stream().map(EmailData::getEmail).toList();
        this.phones = user.getPhones().stream().map(PhoneData::getPhone).toList();
        this.balance = user.getAccount().getBalance();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getEmails() {
        return emails;
    }

    public List<String> getPhones() {
        return phones;
    }

    public BigDecimal getBalance() { return balance; }

}
