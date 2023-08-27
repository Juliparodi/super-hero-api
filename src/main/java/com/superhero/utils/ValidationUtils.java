package com.superhero.utils;

import com.superhero.constants.ExceptionConstants;
import com.superhero.exception.ValidationException;
import org.springframework.util.StringUtils;

public class ValidationUtils {

    private ValidationUtils(){}

    public static void validateLong(Long id) {
        if (id == null) {
            throw new ValidationException(ExceptionConstants.VALIDATE_PARAMS_NOT_EMPTY);
        }
    }

    public static void validateString(String value) {
        if (!StringUtils.hasText(value)) {
            throw new ValidationException(ExceptionConstants.VALIDATE_PARAMS_NOT_EMPTY);
        }
    }

}
