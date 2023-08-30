package com.superhero.utils;

import org.springframework.security.core.Authentication;

public interface JwtUtilsWrapper {
    String createToken(Authentication authResult);
    void validateAndSetAuthentication(String jwt);
}
