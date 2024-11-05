package com.superhero.controller;

import com.superhero.config.kubernetes.CustomHealthIndicator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthController {

    private final CustomHealthIndicator customHealthIndicator;

    @GetMapping("/actuator/custom-health")
    public Health customHealth() {
        return customHealthIndicator.health();
    }
}
