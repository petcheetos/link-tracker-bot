package edu.java.bot.configuration;

import edu.java.bot.retry_model.ScrapperRetryPolicy;
import edu.java.bot.retry_model.ScrapperRetryPolicyData;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.http.HttpStatus;

public class ScrapperRetryPolicyConfig {
    private static final int INTERVAL = 5000;

    private ScrapperRetryPolicyConfig() {
    }

    public static Retry config(ScrapperRetryPolicyData retryPolicyData) {
        RetryConfig config;
        if (retryPolicyData.getRetryPolicy().equals(ScrapperRetryPolicy.CONSTANT)) {
            config = constant(retryPolicyData);
        } else if (retryPolicyData.getRetryPolicy().equals(ScrapperRetryPolicy.LINEAR)) {
            config = linear(retryPolicyData);
        } else {
            config = exponential(retryPolicyData);
        }
        return Retry.of("bot-retry", config);
    }

    private static Predicate<Object> predicate(List<HttpStatus> allowedStatuses) {
        return result -> {
            if (result instanceof HttpStatus status) {
                return allowedStatuses.contains(status);
            }
            return false;
        };
    }

    public static RetryConfig constant(ScrapperRetryPolicyData retryPolicyData) {
        Predicate<Object> httpStatusPredicate = predicate(retryPolicyData.getHttpStatuses());
        return RetryConfig.custom()
            .waitDuration(Duration.ofMillis(INTERVAL))
            .maxAttempts(retryPolicyData.getAttempts())
            .retryOnResult(httpStatusPredicate)
            .build();
    }

    public static RetryConfig linear(ScrapperRetryPolicyData retryPolicyData) {
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

    public static RetryConfig exponential(ScrapperRetryPolicyData retryPolicyData) {
        Predicate<Object> httpStatusPredicate = predicate(retryPolicyData.getHttpStatuses());
        return RetryConfig.custom()
            .intervalFunction(IntervalFunction.ofExponentialBackoff(INTERVAL))
            .maxAttempts(retryPolicyData.getAttempts())
            .retryOnResult(httpStatusPredicate)
            .build();
    }
}
