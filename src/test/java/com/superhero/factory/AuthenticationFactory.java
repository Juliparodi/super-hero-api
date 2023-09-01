package com.superhero.factory;


import com.superhero.utils.JwtUtils;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AuthenticationFactory {

    public static Authentication createAuthRoleRead() {
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_read"));
        return new UsernamePasswordAuthenticationToken(
            new User("user", "user123", authorities),
            null,
            authorities
        );
    }

    public static Authentication createAuthRoleAdmin() {
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_admin"));
        return new UsernamePasswordAuthenticationToken(
            new User("admin", "admin123", authorities),
            null,
            authorities
        );
    }
}
