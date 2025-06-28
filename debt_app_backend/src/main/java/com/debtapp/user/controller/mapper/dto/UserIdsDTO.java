package com.debtapp.user.controller.mapper.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserIdsDTO {
    private List<Integer> usersIds;
}
