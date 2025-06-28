package com.debtapp.debt.controller.dto;

import com.debtapp.debt.dao.model.Debt;

public class DebtMapper {
    public static DebtDTO map(Debt dao) {
        return DebtDTO.builder()
                .id(dao.getId())
                .title(dao.getTitle())
                .amount(dao.getAmount())
                .receiver(dao.getReceiver())
                .creator(dao.getCreditorUser().getUsername())
                .build();
    }
}
