package com.debtapp.debt.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebtSummaryDTO {
    private Integer user_id;
    private Double total_debt;
}

