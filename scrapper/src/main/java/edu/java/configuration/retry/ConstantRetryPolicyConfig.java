package edu.java.configuration.retry;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "api.bot.retry-policy", havingValue = "constant")
public class ConstantRetryPolicyConfig implements RetryPolicyConfig {

    private final HttpStatusPredicate predicate;

    @Value("${api.bot.interval}")
    private int interval;
    @Value(value = "${api.bot.attempts}")
    private int attempts;
    @Value("#{'${api.bot.http-statuses}'.split(',')}")
    private List<HttpStatus> httpStatuses;

    @Override
    public Retry configure() {
        Predicate<Object> httpStatusPredicate = predicate.predicate(httpStatuses);
        RetryConfig retryConfig = RetryConfig.custom()
            .waitDuration(Duration.ofMillis(interval))
            .maxAttempts(attempts)
            .retryOnResult(httpStatusPredicate)
            .build();
        return Retry.of("scrapper-retry", retryConfig);
    }
}
