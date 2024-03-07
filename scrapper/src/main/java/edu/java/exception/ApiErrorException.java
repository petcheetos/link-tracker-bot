package edu.java.exception;

import edu.java.models.ApiErrorResponse;

public class ApiErrorException extends RuntimeException {

    public ApiErrorException(ApiErrorResponse apiErrorResponse) {
        super(apiErrorResponse.exceptionMessage());
    }
}
