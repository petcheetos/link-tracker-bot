package edu.java.bot.configuration.retry;

import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "api.scrapper.retry-policy", havingValue = "linear")
public class LinearRetryPolicyConfig implements RetryPolicyConfig {

    private final HttpStatusPredicate predicate;

    @Value("${api.scrapper.interval}")
    private int interval;
    @Value(value = "${api.scrapper.attempts}")
    private int attempts;
    @Value("#{'${api.scrapper.http-statuses}'.split(',')}")
    private List<HttpStatus> httpStatuses;

    @Override
    public Retry configure() {
        Predicate<Object> httpStatusPredicate = predicate.predicate(httpStatuses);
        RetryConfig retryConfig = RetryConfig.custom()
            .intervalFunction(IntervalFunction.of(
                Duration.ofMillis(interval),
                attempt -> interval + attempt * interval
            ))
            .maxAttempts(attempts)
            .retryOnResult(httpStatusPredicate)
            .build();
        return Retry.of("bot-retry", retryConfig);
    }
}
