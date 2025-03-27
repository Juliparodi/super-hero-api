package com.superhero.unit.exception;

import com.superhero.constants.ExceptionConstants;
import com.superhero.exception.GlobalExceptionHandler;
import com.superhero.exception.SuperHeroNotFoundException;
import com.superhero.exception.ValidationException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler handler;

  @BeforeEach
  void setup() {
    handler = new GlobalExceptionHandler();
  }

  @Test
  void testHandleSuperHeroNotFoundException() {
    SuperHeroNotFoundException ex = new SuperHeroNotFoundException("Superhero not found");

    ResponseEntity<String> response = handler.handleSuperHeroNotFoundException(ex);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Superhero not found", response.getBody());
  }

  @Test
  void testHandleGenericException() {
    Exception ex = new Exception("Something went wrong");

    ResponseEntity<String> response = handler.handleException(ex);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals(ExceptionConstants.INTERNAL_SERVER_ERROR, response.getBody());
  }

  @Test
  void testHandleValidationException() {
    ValidationException ex = new ValidationException("Invalid input");

    ResponseEntity<String> response = handler.handleValidationException(ex);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Invalid input", response.getBody());
  }

  @Test
  void testHandleConstraintViolationException() {
    ConstraintViolationException ex = new ConstraintViolationException("Constraint failed", null);

    ResponseEntity<String> response = handler.handleValidationException(ex);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Constraint failed", response.getBody());
  }

  @Test
  void testHandleMethodArgumentNotValidException() {
    // mock BindingResult with field error
    FieldError fieldError = new FieldError("superHero", "name", "Name must not be blank");
    BindingResult bindingResult = mock(BindingResult.class);
    when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

    MethodArgumentNotValidException ex = new MethodArgumentNotValidException(
        mock(MethodParameter.class),
        bindingResult
    );

    ResponseEntity<String> response = handler.handleValidationException(ex);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Name must not be blank", response.getBody());
  }
}
