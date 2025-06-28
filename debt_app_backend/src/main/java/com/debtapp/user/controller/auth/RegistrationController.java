package com.debtapp.user.controller.auth;

import com.debtapp.user.controller.auth.mapper.dto.AuthToken;
import com.debtapp.user.controller.auth.mapper.dto.UserRegistrationRequestDTO;
import com.debtapp.user.service.UserAuthorizationService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private static final Logger log = LogManager.getLogger(RegistrationController.class);
    private final UserAuthorizationService userAuthorizationService;

    @PostMapping
    public ResponseEntity<AuthToken> registerUser(
            @RequestBody UserRegistrationRequestDTO request
    ) {
        log.info("Received a request to register user with email: {}", request.getEmail());
        return ResponseEntity.ok(userAuthorizationService.registerUser(request));
    }
}
