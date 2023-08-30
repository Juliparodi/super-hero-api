package com.superhero.utils;

import org.springframework.security.core.Authentication;

public class AuthenticationUtil {

    private final JwtUtilsWrapper jwtUtilsWrapper;

    public AuthenticationUtil(JwtUtilsWrapper jwtUtilsWrapper) {
        this.jwtUtilsWrapper = jwtUtilsWrapper;
    }

    public String createToken(Authentication mockAuth) {
        return jwtUtilsWrapper.createToken(mockAuth);
    }
}
