package edu.java.configuration.retry;

import java.util.List;
import java.util.function.Predicate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class HttpStatusPredicate {
    public Predicate<Object> predicate(List<HttpStatus> allowedStatuses) {
        return result -> {
            if (result instanceof HttpStatus status) {
                return allowedStatuses.contains(status);
            }
            return false;
        };
    }
}
