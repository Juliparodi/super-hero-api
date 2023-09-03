package com.superhero.config.security.filters;

import static com.superhero.constants.SecurityConstants.BEARER;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.superhero.utils.JwtUtilsWrapper;
import com.superhero.constants.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class CustomJwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private ObjectMapper objectMapper;
    private final JwtUtilsWrapper jwtUtils;

    @Autowired
    public CustomJwtAuthenticationFilter(
        AuthenticationManager authenticationManager,
        JwtUtilsWrapper jwtUtils) {
        super.setAuthenticationManager(authenticationManager);
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void successfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain,
        Authentication authResult) throws IOException {

        if (null != authResult) {
            String jwt = jwtUtils.createToken(authResult);
            response.setHeader(SecurityConstants.JWT_HEADER, jwt);
            writeResponse(response, jwt);
        }

    }

    private void writeResponse(HttpServletResponse response, String jwt) throws IOException {
        objectMapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(
            Map.of(
                "token", jwt,
                "type", BEARER
            )
        ));
        response.getWriter().flush();
    }

}
