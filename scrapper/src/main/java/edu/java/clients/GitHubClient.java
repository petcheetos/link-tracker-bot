package edu.java.clients;

import edu.java.configuration.retry.RetryPolicyConfig;
import edu.java.dto.GitHubResponse;
import io.github.resilience4j.retry.Retry;
import jakarta.annotation.PostConstruct;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClient {
    private static final String BASE_URL = "https://api.github.com";
    private final WebClient webClient;

    @Autowired
    private RetryPolicyConfig retryPolicyConfig;

    private Retry retry;

    public GitHubClient(WebClient.Builder builder, String baseUrl) {
        this.webClient = builder
            .baseUrl(Objects.requireNonNullElse(baseUrl, BASE_URL))
            .build();
    }

    @PostConstruct
    private void initRetry() {
        retry = retryPolicyConfig.configure();
    }

    public GitHubResponse getRepositoryInfo(String owner, String repo) {
        return retry.executeSupplier(() -> webClient
            .get()
            .uri(owner + "/" + repo)
            .retrieve()
            .bodyToMono(GitHubResponse.class)
            .block());
    }
}
