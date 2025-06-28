package com.debtapp.user.controller.auth.mapper.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String email;
    private String password;
}
