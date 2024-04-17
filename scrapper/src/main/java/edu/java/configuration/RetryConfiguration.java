package edu.java.configuration;

import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
@RequiredArgsConstructor
@SuppressWarnings("MultipleStringLiterals")
public class RetryConfiguration {

    @Value("${api.bot.interval}")
    private int interval;
    @Value(value = "${api.bot.attempts}")
    private int attempts;
    @Value("#{'${api.bot.http-statuses}'.split(',')}")
    private List<HttpStatus> httpStatuses;

    public RetryConfiguration(int interval, int attempts, List<HttpStatus> httpStatuses) {
        this.interval = interval;
        this.attempts = attempts;
        this.httpStatuses = httpStatuses;
    }

    @ConditionalOnProperty(name = "api.bot.retry-policy", havingValue = "constant")
    @Bean
    public Retry constant() {
        Predicate<Object> httpStatusPredicate = predicate(httpStatuses);
        RetryConfig retryConfig = RetryConfig.custom()
            .waitDuration(Duration.ofMillis(interval))
            .maxAttempts(attempts)
            .retryOnResult(httpStatusPredicate)
            .build();
        return Retry.of("scrapper-retry", retryConfig);
    }

    @ConditionalOnProperty(name = "api.bot.retry-policy", havingValue = "linear")
    @Bean
    public Retry linear() {
        Predicate<Object> httpStatusPredicate = predicate(httpStatuses);
        RetryConfig retryConfig = RetryConfig.custom()
            .intervalFunction(IntervalFunction.of(
                Duration.ofMillis(interval),
                attempt -> interval + attempt * interval
            ))
            .maxAttempts(attempts)
            .retryOnResult(httpStatusPredicate)
            .build();
        return Retry.of("scrapper-retry", retryConfig);
    }

    @ConditionalOnProperty(name = "api.bot.retry-policy", havingValue = "exponential")
    @Bean
    public Retry exponential() {
        Predicate<Object> httpStatusPredicate = predicate(httpStatuses);
        RetryConfig retryConfig = RetryConfig.custom()
            .intervalFunction(IntervalFunction.ofExponentialBackoff(interval))
            .maxAttempts(attempts)
            .retryOnResult(httpStatusPredicate)
            .build();
        return Retry.of("scrapper-retry", retryConfig);
    }

    private Predicate<Object> predicate(List<HttpStatus> allowedStatuses) {
        return result -> {
            if (result instanceof HttpStatus status) {
                return allowedStatuses.contains(status);
            }
            return false;
        };
    }
}
