package com.superhero.utils;

import com.superhero.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationUtilsTest {

  @Test
  void testValidateString_noTextFound() {
    assertThrows(ValidationException.class, () -> ValidationUtils.validateStringConvertToLong(""));
  }

  @Test
  void testValidateStringConvertToLong_returningLongNumber() {
    assertEquals(3L, ValidationUtils.validateStringConvertToLong("3"));
  }

  @Test
  void testValidateStringConvertToLongForAString_returningNumberFormatException() {
    assertThrows(ValidationException.class, () -> ValidationUtils.validateStringConvertToLong("Hola"));
  }
}
