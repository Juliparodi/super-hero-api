package com.superhero.exception;

public class SuperHeroNotFoundException extends RuntimeException{
    public SuperHeroNotFoundException(String message) {
        super(message);
    }

}
