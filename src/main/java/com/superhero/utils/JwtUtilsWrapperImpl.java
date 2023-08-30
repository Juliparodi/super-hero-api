package com.superhero.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtUtilsWrapperImpl implements JwtUtilsWrapper {

    private final JwtUtils jwtUtils;

    @Autowired
    public JwtUtilsWrapperImpl(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public String createToken(Authentication authResult) {
        return jwtUtils.createToken(authResult);
    }

    @Override
    public void validateAndSetAuthentication(String jwt) {
        jwtUtils.validateAndSetAuthentication(jwt);
    }
}
