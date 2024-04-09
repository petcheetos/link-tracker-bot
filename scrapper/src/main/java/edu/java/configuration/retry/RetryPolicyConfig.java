package edu.java.configuration.retry;

import io.github.resilience4j.retry.Retry;

public interface RetryPolicyConfig {
    Retry configure();
}
