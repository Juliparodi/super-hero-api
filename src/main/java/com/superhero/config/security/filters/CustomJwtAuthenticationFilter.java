package com.superhero.config.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.superhero.constants.SecurityConstants;
import com.superhero.utils.JwtUtilsWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static com.superhero.constants.SecurityConstants.BEARER;

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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String url = httpRequest.getRequestURI();

        if (shouldSkip(url)) {
            chain.doFilter(request, response);
            return;
        }

        super.doFilter(request, response, chain);
    }

    private boolean shouldSkip(String url) {
        return "/login".equals(url)
            || url.contains("/swagger-ui")
            || url.contains("/v3")
            || url.contains("/actuator")
            || url.contains("/targets")
            || url.contains("/h2-console");
    }



}
