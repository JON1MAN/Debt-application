package com.debtapp.user.controller;

import com.debtapp.user.controller.mapper.UserMapper;
import com.debtapp.user.controller.mapper.dto.UserDTO;
import com.debtapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.debtapp.user.controller.mapper.UserMapper.map;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private static final Logger log = LogManager.getLogger(UserController.class);

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer userId) {
        log.info("Received a request to get user by id: {}", userId);
        return ResponseEntity.ok(map(userService.findById(userId)));
    }

    @GetMapping
    public ResponseEntity<Map<String, List<UserDTO>>> getAllUsers() {
        log.info("Received a request to get all users");
        var result = userService.findAllUsers().stream()
                .map(UserMapper::map)
                .toList();
        Map<String, List<UserDTO>> response = new HashMap<>();
        response.put("users", result);
        return ResponseEntity.ok(response);
    }
}
