package com.debtapp.user.controller.auth;

import com.debtapp.config.security.jwt.JWTService;
import com.debtapp.user.controller.auth.mapper.dto.AuthToken;
import com.debtapp.user.controller.auth.mapper.dto.UserLoginDTO;
import com.debtapp.user.service.UserAuthorizationService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthorizationController {
    private final UserAuthorizationService userAuthorizationService;
    private final JWTService jwtService;
    private static final Logger log = LogManager.getLogger(AuthorizationController.class);

    @PostMapping("/login")
    public ResponseEntity<AuthToken> authenticateUser(@RequestBody UserLoginDTO request) {
        log.info("Received a request to authorize user with email: {}", request.getEmail());
        return ResponseEntity.ok(userAuthorizationService.authenticate(request));
    }

    @GetMapping("/verifyToken/{token}")
    public ResponseEntity<Map<String, String>> verifyToken(@PathVariable String token) {
        log.info("Received a request to verify jwt token: {}", token);
        try {
            jwtService.extractAllClaims(token);
            return ResponseEntity.ok(Map.of("message", "Token is valid"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token", "error", e.getMessage()));
        }
    }
}
