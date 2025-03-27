package com.superhero.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsWrapperImplTest {

  @Mock
  JwtUtils jwtUtils;

  @InjectMocks
  JwtUtilsWrapperImpl jwtUtilsWrapper;

  @Test
  void testCreateToken_returningToken() {

    when(jwtUtils.createToken(any())).thenReturn("thisIsASecretJwtKeyThatShouldBeLongEnoughForHmacShaKey");

    assertEquals("thisIsASecretJwtKeyThatShouldBeLongEnoughForHmacShaKey",  jwtUtilsWrapper.createToken(Mockito.mock(Authentication.class)));
  }

  @Test
  void testValidateAndSetAuthentication() {
    doNothing().when(jwtUtils).validateAndSetAuthentication("thisIsASecretJwtKeyThatShouldBeLongEnoughForHmacShaKey");

    jwtUtilsWrapper.validateAndSetAuthentication("thisIsASecretJwtKeyThatShouldBeLongEnoughForHmacShaKey");

    verify(jwtUtils, times(1)).validateAndSetAuthentication("thisIsASecretJwtKeyThatShouldBeLongEnoughForHmacShaKey");
  }
}
