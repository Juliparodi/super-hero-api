package com.superhero.exception;

import static com.superhero.constants.ExceptionConstants.UNAUTHORIZED_CALL;

public class NoTokenException extends RuntimeException {

    public NoTokenException() {
        super(UNAUTHORIZED_CALL);
    }

}
