package com.yourcompany.phonebooking.web;

public class ResponseError {

    private String message;

    public ResponseError() {
    }

    public ResponseError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
