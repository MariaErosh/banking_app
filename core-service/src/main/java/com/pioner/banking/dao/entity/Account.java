package com.pioner.banking.dao.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "balance", precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(name = "init_balance", precision = 19, scale = 2)
    private BigDecimal initBalance;

    @Column(name = "currency_code")
    private String currencyCode;

    public Account(BigDecimal balance, User user) {
        this.balance = balance;
        this.initBalance = balance;
        this.user = user;
        this.currencyCode = "EUR";
    }

    public Account(BigDecimal balance, String currencyCode, User user) {
        this.balance = balance;
        this.initBalance = balance;
        this.user = user;
        this.currencyCode = currencyCode;
    }

    protected Account() {
        // jpa
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getInitBalance() {
        return initBalance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCurrencyCode(String currency_code) { this.currencyCode = currency_code; }

    public String getCurrencyCode() { return this.currencyCode; }

}
