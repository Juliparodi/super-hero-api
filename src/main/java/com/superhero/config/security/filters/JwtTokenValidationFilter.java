package com.superhero.config.security.filters;

import static com.superhero.constants.ExceptionConstants.INVALID_TOKEN;
import static com.superhero.constants.SecurityConstants.JWT_HEADER;

import com.superhero.utils.JwtUtilsWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtTokenValidationFilter extends OncePerRequestFilter {
    private final JwtUtilsWrapper jwtUtils;

    public JwtTokenValidationFilter(JwtUtilsWrapper jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String jwt = request.getHeader(JWT_HEADER);

        if (jwt != null && jwt.startsWith("Bearer ")) {
            try {
                jwtUtils.validateAndSetAuthentication(jwt);
            }  catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(String.format(INVALID_TOKEN, jwt));
                response.getWriter().flush();
            }

        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String url = request.getRequestURI();
        return "/login".equals(url)
            || "/swagger-ui/index.html".equalsIgnoreCase(url);
    }



}
