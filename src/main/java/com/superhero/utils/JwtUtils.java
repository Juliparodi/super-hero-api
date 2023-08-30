package com.superhero.utils;

import static com.superhero.constants.SecurityConstants.BEARER;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    private final String jwtSecretKey;

    @Autowired
    public JwtUtils(@Value("${jwt.key}") String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    public String createToken(Authentication authResult) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
            .setIssuer("Juli Parodi")
            .setSubject("JWT Token")
            .claim("username", authResult.getName())
            .claim("authorities", populateAuthorities(authResult.getAuthorities()))
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + 30000000))
            .signWith(key).compact();

    }
    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }

    public void validateAndSetAuthentication(String jwt) {
        SecretKey key = Keys.hmacShaKeyFor(
            jwtSecretKey.getBytes(StandardCharsets.UTF_8));
        String jwtToken = jwt.substring(BEARER.length());

        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(jwtToken)
            .getBody();
        String username = String.valueOf(claims.get("username"));
        String authorities = (String) claims.get("authorities");
        Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
            AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
