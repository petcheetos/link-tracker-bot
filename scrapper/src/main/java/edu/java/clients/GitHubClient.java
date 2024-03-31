package edu.java.clients;

import edu.java.configuration.RetryPolicyConfig;
import edu.java.dto.GitHubResponse;
import edu.java.retry_model.RetryPolicy;
import edu.java.retry_model.RetryPolicyData;
import io.github.resilience4j.retry.Retry;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClient {
    private static final String BASE_URL = "https://api.github.com";
    private final WebClient webClient;

    private Retry retry;

    @Value(value = "${api.github.retry-policy}")
    private RetryPolicy retryPolicy;

    @Value(value = "${api.github.attempts}")
    private int attempts;

    @Value("#{'${api.github.http-statuses}'.split(',')}")
    private List<HttpStatus> httpStatuses;

    public GitHubClient(WebClient.Builder builder, String baseUrl) {
        this.webClient = builder
            .baseUrl(Objects.requireNonNullElse(baseUrl, BASE_URL))
            .build();
    }

    @PostConstruct
    private void initRetry() {
        RetryPolicyData retryPolicyData = new RetryPolicyData();
        retryPolicyData.setRetryPolicy(retryPolicy);
        retryPolicyData.setAttempts(attempts);
        retryPolicyData.setHttpStatuses(httpStatuses);
        retry = RetryPolicyConfig.config(retryPolicyData);
    }

    public GitHubResponse getRepositoryInfo(String owner, String repo) {
        return webClient.get()
            .uri(owner + "/" + repo)
            .retrieve()
            .bodyToMono(GitHubResponse.class)
            .block();
    }

    public GitHubResponse retryGetRepositoryInfo(String owner, String repo) {
        return Retry.decorateSupplier(retry, () -> getRepositoryInfo(owner, repo)).get();
    }
}
