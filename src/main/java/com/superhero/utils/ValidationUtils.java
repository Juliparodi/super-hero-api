package com.superhero.utils;

import static com.superhero.constants.ExceptionConstants.VALIDATE_PARAMS_NOT_EMPTY;
import static com.superhero.constants.ExceptionConstants.VALIDATE_PARAMS_NOT_NUMBER;

import com.superhero.exception.ValidationException;
import org.springframework.util.StringUtils;

public class ValidationUtils {

    private ValidationUtils(){}

    public static void validateString(String value) {
        if (!StringUtils.hasText(value)) {
            throw new ValidationException(VALIDATE_PARAMS_NOT_EMPTY);
        }
    }
    public static Long validateStringConvertToLong(String id) {
        validateString(id);

        try {
            return Long.parseLong(id);
        } catch (NumberFormatException ex) {
            throw new ValidationException(VALIDATE_PARAMS_NOT_NUMBER);
        }
    }
}
