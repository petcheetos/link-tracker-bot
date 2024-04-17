package edu.java.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@EnableScheduling
public record ApplicationConfig(
    @Bean
    @NotNull
    Scheduler scheduler,
    @NotNull
    GithubBaseUrl githubBaseUrl,
    @NotNull
    StackoverflowBaseUrl stackoverflowBaseUrl,
    @NotNull
    BotBaseUrl botBaseUrl,
    @NotNull
    AccessType databaseAccessType
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record GithubBaseUrl(@NotNull String baseUrl) {
    }

    public record StackoverflowBaseUrl(@NotNull String baseUrl) {
    }

    public record BotBaseUrl(@NotNull String baseUrl) {
    }

    public enum AccessType {
        JDBC, JPA
    }
}
