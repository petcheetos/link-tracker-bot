package edu.java.exception;

public class ApiErrorException extends RuntimeException {

    public ApiErrorException(String message) {
        super(message);
    }
}
