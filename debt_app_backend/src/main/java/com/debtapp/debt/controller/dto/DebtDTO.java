package com.debtapp.debt.controller.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DebtDTO {
    private Integer id;
    private String title;
    private String receiver;
    private String creator;
    private Double amount;
}
