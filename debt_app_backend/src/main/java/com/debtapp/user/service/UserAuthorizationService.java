package com.debtapp.user.service;

import com.debtapp.config.security.SecurityUser;
import com.debtapp.config.security.SecurityUserService;
import com.debtapp.config.security.jwt.JWTService;
import com.debtapp.user.controller.auth.mapper.dto.AuthToken;
import com.debtapp.user.controller.auth.mapper.dto.UserLoginDTO;
import com.debtapp.user.controller.auth.mapper.dto.UserRegistrationRequestDTO;
import com.debtapp.user.dao.model.AbstractEntity;
import com.debtapp.user.dao.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthorizationService {

    private static final Logger log = LogManager.getLogger(UserAuthorizationService.class);
    private final AuthenticationProvider authenticationProvider;
    private final UserService userService;
    private final JWTService jwtService;

    public AuthToken registerUser(UserRegistrationRequestDTO dto) {
        log.info("Registering user with email: {}", dto.getEmail());

        User user = userService.createUser(dto);
        SecurityUser securityUser = SecurityUserService.createSecurityUser(user);

        return jwtService.generateAuthTokens(securityUser);
    }

    public AuthToken authenticate(UserLoginDTO dto) {
        log.info("Authenticating user with email: {}", dto.getEmail());

        try {
            authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getEmail(),
                            dto.getPassword()
                    )
            );
        } catch (AuthenticationException ex) {
            log.error("Invalid password or email provided for email: {}", dto.getEmail());
            throw new BadCredentialsException("Invalid email or password provided");
        }

        User user = userService.findByEmail(dto.getEmail());
        SecurityUser securityUser = SecurityUserService.createSecurityUser(user);

        return jwtService.generateAuthTokens(securityUser);
    }

}
