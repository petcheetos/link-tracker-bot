package edu.java.configuration;

import edu.java.retry_model.RetryPolicy;
import edu.java.retry_model.RetryPolicyData;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.http.HttpStatus;

public class RetryPolicyConfig {
    private static final int INTERVAL = 5000;

    private RetryPolicyConfig() {
    }

    public static Retry config(RetryPolicyData retryPolicyData) {
        RetryConfig config;
        if (retryPolicyData.getRetryPolicy().equals(RetryPolicy.CONSTANT)) {
            config = constant(retryPolicyData);
        } else if (retryPolicyData.getRetryPolicy().equals(RetryPolicy.LINEAR)) {
            config = linear(retryPolicyData);
        } else {
            config = exponential(retryPolicyData);
        }
        return Retry.of("scrapper-retry", config);
    }

    private static Predicate<Object> predicate(List<HttpStatus> allowedStatuses) {
        return result -> {
            if (result instanceof HttpStatus status) {
                return allowedStatuses.contains(status);
            }
            return false;
        };
    }

    public static RetryConfig constant(RetryPolicyData retryPolicyData) {
        Predicate<Object> httpStatusPredicate = predicate(retryPolicyData.getHttpStatuses());
        return RetryConfig.custom()
            .waitDuration(Duration.ofMillis(INTERVAL))
            .maxAttempts(retryPolicyData.getAttempts())
            .retryOnResult(httpStatusPredicate)
            .build();
    }

    public static RetryConfig linear(RetryPolicyData retryPolicyData) {
        Predicate<Object> httpStatusPredicate = predicate(retryPolicyData.getHttpStatuses());
        return RetryConfig.custom()
            .intervalFunction(IntervalFunction.of(
                Duration.ofMillis(INTERVAL),
                attempt -> INTERVAL + attempt * INTERVAL
            ))
            .maxAttempts(retryPolicyData.getAttempts())
            .retryOnResult(httpStatusPredicate)
            .build();
    }

    public static RetryConfig exponential(RetryPolicyData retryPolicyData) {
        Predicate<Object> httpStatusPredicate = predicate(retryPolicyData.getHttpStatuses());
        return RetryConfig.custom()
            .intervalFunction(IntervalFunction.ofExponentialBackoff(INTERVAL))
            .maxAttempts(retryPolicyData.getAttempts())
            .retryOnResult(httpStatusPredicate)
            .build();
    }
}
