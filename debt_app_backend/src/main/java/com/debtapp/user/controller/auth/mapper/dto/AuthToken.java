package com.debtapp.user.controller.auth.mapper.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthToken {
    private String access_token;
    private String username;
}
