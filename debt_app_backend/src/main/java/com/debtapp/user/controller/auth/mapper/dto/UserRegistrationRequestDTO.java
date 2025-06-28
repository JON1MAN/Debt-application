package com.debtapp.user.controller.auth.mapper.dto;

import lombok.Data;

@Data
public class UserRegistrationRequestDTO {
    private String username;
    private String email;
    private String password;
}
