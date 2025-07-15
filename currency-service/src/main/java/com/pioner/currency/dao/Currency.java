package com.pioner.currency.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "currencies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, nullable = false)
    private String isoCurrency;


    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.LAZY)
    private List<ExchangeRate> exchangeRates;


}
