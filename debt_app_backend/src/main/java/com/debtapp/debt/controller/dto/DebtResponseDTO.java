package com.debtapp.debt.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebtResponseDTO {
    private Integer debtor;
    private Integer creditor;
    private double amount;
}

