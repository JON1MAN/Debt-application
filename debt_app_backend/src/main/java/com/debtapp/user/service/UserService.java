package com.debtapp.user.service;

import com.debtapp.user.controller.auth.mapper.dto.UserRegistrationRequestDTO;
import com.debtapp.user.controller.exceptions.UserNotFoundException;
import com.debtapp.user.controller.mapper.UserMapper;
import com.debtapp.user.controller.exceptions.UserAlreadyExistsException;
import com.debtapp.user.controller.exceptions.UsernameNotFoundException;
import com.debtapp.user.dao.model.User;
import com.debtapp.user.dao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private static final Logger log = LogManager.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;

    public List<User> findAllUsers() {
        log.info("Fetching all users from database");
        return userRepository.findAll();
    }

    public List<User> findAllUsers(List<Integer> usersIds) {
        log.info("Fetching all users from database with provided ids");
        return userRepository.findAllById(usersIds);
    }

    public User findById(Integer id) {
        log.info("Fetching user with id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", id);
                    return new UserNotFoundException("User with provided id not found");
                });
    }

    public User findByEmail(String email) {
        log.info("Fetching user with email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", email);
                    return new UsernameNotFoundException("user with provided email not found");
                });
    }

    public User createUser(UserRegistrationRequestDTO dto) {
        log.info("Creating user with email: {}", dto.getEmail());

        Optional<User> userOptional = userRepository.findByEmail(dto.getEmail());

        if (userOptional.isPresent()) {
            log.error("User already exists with email: {}", dto.getEmail());
            throw new UserAlreadyExistsException("User already exists with email: " + dto.getEmail());
        }

        User user = UserMapper.map(dto);
        user.setHashedPassword(passwordEncoder.encode(dto.getPassword()));

        return userRepository.save(user);
    }
}
