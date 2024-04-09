package edu.java.bot.configuration.retry;

import io.github.resilience4j.retry.Retry;

public interface RetryPolicyConfig {
    Retry configure();
}
