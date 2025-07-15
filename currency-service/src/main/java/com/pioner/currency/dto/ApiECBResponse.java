package com.pioner.currency.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ApiECBResponse {
    private double amount;

    private String base;

    private String date;

    private Map<String, Double> rates;
}
