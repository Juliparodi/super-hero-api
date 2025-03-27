package com.superhero.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {

  private JwtUtils jwtUtils;

  private Authentication authentication;

  private final String SECRET = "thisIsASecretJwtKeyThatShouldBeLongEnoughForHmacShaKey";

  @BeforeEach
  void setup() {
    jwtUtils = new JwtUtils(SECRET);
  }

  @Test
  void testCreateToken_returnsValidJwt() {
    Authentication authentication = Mockito.mock(Authentication.class);
    Mockito.when(authentication.getName()).thenReturn("juli");


    String token = jwtUtils.createToken(authentication);

    assertNotNull(token);
    assertTrue(token.startsWith("ey")); // basic sanity check

    Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

    assertEquals("Juli Parodi", claims.getIssuer());
    assertEquals("JWT Token", claims.getSubject());
    assertEquals("juli", claims.get("username"));
  }


  @Test
  void testValidateAndSetAuthentication_setsContextWithValidJwt() {
    Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    String jwt = Jwts.builder()
        .setIssuer("Juli Parodi")
        .setSubject("JWT Token")
        .claim("username", "juli")
        .claim("authorities", "ROLE_USER,ROLE_ADMIN")
        .setIssuedAt(new java.util.Date())
        .setExpiration(new java.util.Date(System.currentTimeMillis() + 60000))
        .signWith(key)
        .compact();

    String bearerToken = "Bearer " + jwt;

    jwtUtils.validateAndSetAuthentication(bearerToken);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    assertNotNull(authentication);
    assertEquals("juli", authentication.getPrincipal());
    assertTrue(authentication.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    assertTrue(authentication.isAuthenticated());
  }
}
