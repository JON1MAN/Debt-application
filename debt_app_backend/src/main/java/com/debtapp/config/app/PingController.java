package com.debtapp.config.app;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/ping")
@RequiredArgsConstructor
public class PingController {

    private static final Logger log = LogManager.getLogger(PingController.class);

    @GetMapping
    public ResponseEntity<String> getPong() {
        log.info("Received a ping request");
        return ResponseEntity.ok("pong");
    }
}
