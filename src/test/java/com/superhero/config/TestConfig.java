package com.superhero.config;

import com.superhero.utils.AuthenticationUtil;
import com.superhero.utils.JwtUtilsWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public AuthenticationUtil authenticationUtil(JwtUtilsWrapper jwtUtilsWrapper) {
        return new AuthenticationUtil(jwtUtilsWrapper);
    }

}
