package com.superhero.utils;

import com.superhero.exception.ValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import static com.superhero.constants.ExceptionConstants.VALIDATE_PARAMS_NOT_EMPTY;
import static com.superhero.constants.ExceptionConstants.VALIDATE_PARAMS_NOT_NUMBER;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtils {

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
