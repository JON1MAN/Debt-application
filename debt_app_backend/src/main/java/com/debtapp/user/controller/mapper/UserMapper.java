package com.debtapp.user.controller.mapper;

import com.debtapp.user.controller.auth.mapper.dto.UserRegistrationRequestDTO;
import com.debtapp.user.controller.mapper.dto.UserDTO;
import com.debtapp.user.dao.model.User;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserMapper {

    public static UserDTO map(User dao) {
        return UserDTO.builder()
                .id(dao.getId())
                .username(dao.getEmail())
                .build();
    }

    public static User map(UserRegistrationRequestDTO dto) {
        User dao = new User();

        dao.setEmail(dto.getEmail());
        dao.setUsername(dto.getUsername());

        return dao;
    }
}
