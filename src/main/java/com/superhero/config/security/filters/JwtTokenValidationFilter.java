package com.superhero.config.security.filters;

import static com.superhero.constants.ExceptionConstants.INVALID_TOKEN;
import static com.superhero.constants.ExceptionConstants.UNAUTHORIZED_CALL;
import static com.superhero.constants.SecurityConstants.JWT_HEADER;

import com.superhero.exception.NoTokenException;
import com.superhero.utils.JwtUtilsWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
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
        throws IOException, ServletException {
        String jwt = request.getHeader(JWT_HEADER);

        if (jwt != null) {
            try {
                if (jwt.startsWith("Bearer ")) {
                    jwtUtils.validateAndSetAuthentication(jwt);
                } else {
                    addingInvalidTokenResponse(response, jwt);
                }
            }  catch (Exception e) {
                addingInvalidTokenResponse(response, jwt);
                return;
            }
        } else {
            addingNoTokenResponse(response);
            return;
        }

        filterChain.doFilter(request, response);

    }

    private void addingInvalidTokenResponse(HttpServletResponse response, String jwt)
        throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(String.format(INVALID_TOKEN, jwt));
        response.getWriter().flush();
    }

    private void addingNoTokenResponse(HttpServletResponse response)
        throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(UNAUTHORIZED_CALL);
        response.getWriter().flush();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String url = request.getRequestURI();
        return "/login".equals(url)
            || url.contains("/swagger-ui")
            || url.contains("/v3");
    }



}
