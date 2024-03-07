package edu.java.exception;

import edu.java.models.ApiErrorResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(LinkNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handle(LinkNotFoundException e) {
        return new ApiErrorResponse(
            "Link not found",
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            e.getClass().getName(),
            e.getMessage(),
            getStacktrace(e)
        );
    }

    @ExceptionHandler(RequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handle(RequestException e) {
        return new ApiErrorResponse(
            "Invalid request",
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            e.getClass().getName(),
            e.getMessage(),
            getStacktrace(e)
        );
    }

    private List<String> getStacktrace(RuntimeException e) {
        List<String> list = new ArrayList<>();
        for (var line : e.getStackTrace()) {
            list.add(line.toString());
        }
        return list;
    }
}
