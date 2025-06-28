package com.debtapp.debt.controller.dto;

import lombok.Data;

@Data
public class DebtCreateRequestDTO {
    private String title;
    private String receiver;
    private Integer receiverId;
    private Double amount;
    private Integer userId;
}

