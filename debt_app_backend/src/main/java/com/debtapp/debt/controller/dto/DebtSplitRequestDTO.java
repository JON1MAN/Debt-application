package com.debtapp.debt.controller.dto;

import lombok.Data;

import java.util.Map;

@Data
public class DebtSplitRequestDTO {
    private double costs;
    private Map<Integer, Double> payments;
}

