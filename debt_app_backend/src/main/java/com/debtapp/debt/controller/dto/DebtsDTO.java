package com.debtapp.debt.controller.dto;

import com.debtapp.debt.dao.model.Debt;
import lombok.Data;

import java.util.List;

@Data
public class DebtsDTO {
    private List<DebtDTO> debts;
}
