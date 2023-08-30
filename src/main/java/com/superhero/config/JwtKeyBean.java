package com.superhero.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtKeyBean {

    @Value("${jwt.key}")
    private String jwtSecretKey;

    @Bean
    public String getJwtSecretKey() {
        return jwtSecretKey;
    }

}
