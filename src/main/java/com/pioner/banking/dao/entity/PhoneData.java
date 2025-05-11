package com.pioner.banking.dao.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "phone_data")
public class PhoneData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "phone")
    private String phone;

    public PhoneData(User user, String phone) {
        this.user = user;
        this.phone = phone;
    }

    protected PhoneData() {
        // jpa
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

}
