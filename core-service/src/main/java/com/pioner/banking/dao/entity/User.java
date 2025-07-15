package com.pioner.banking.dao.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmailData> emails;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneData> phones;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Account account;

    protected User() {
        // jpa
    }

    @Override
    public String toString() {
        return "User + " + id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<EmailData> getEmails() {
        return emails;
    }

    public List<PhoneData> getPhones() {
        return phones;
    }

    public Account getAccount() { return account; };

    public User(String name, LocalDate dateOfBirth, String password,
                List<String> emails, List<String> phones) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth is required");
        }

        if (password == null) {
            throw new IllegalArgumentException("Password is required");
        }
        if (emails == null || emails.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (phones == null || phones.isEmpty()) {
            throw new IllegalArgumentException("Phone is required");
        }

        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.emails = emails.stream().map(email -> new EmailData(this, email)).toList();
        this.phones = phones.stream().map(phone -> new PhoneData(this, phone)).toList();;
        this.account = new Account(BigDecimal.ZERO, this);;
    }

}
