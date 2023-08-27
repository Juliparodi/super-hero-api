package com.superhero.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class SuperHeroNotFoundException extends RuntimeException{

    public SuperHeroNotFoundException(String message) {
        super(message);
    }

}
