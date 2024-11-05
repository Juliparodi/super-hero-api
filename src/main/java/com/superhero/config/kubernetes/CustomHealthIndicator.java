package com.superhero.config.kubernetes;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        if (isConditionMet()) {
            return Health.up().build();
        } else {
            return Health.down().withDetail("reason", "Custom condition not met").build();
        }
    }

    private boolean isConditionMet() {
        return true;
    }
}
