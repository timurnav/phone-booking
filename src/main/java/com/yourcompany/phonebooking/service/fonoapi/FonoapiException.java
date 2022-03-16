package com.yourcompany.phonebooking.service.fonoapi;

public class FonoapiException extends RuntimeException {

    public FonoapiException(String message) {
        super(message);
    }

    public FonoapiException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
