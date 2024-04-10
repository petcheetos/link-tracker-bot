package edu.java.clients;

import edu.java.dto.GitHubResponse;
import io.github.resilience4j.retry.Retry;
import java.util.Objects;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClient {
    private static final String BASE_URL = "https://api.github.com";
    private final WebClient webClient;

    private final Retry retry;

    public GitHubClient(WebClient.Builder builder, String baseUrl, Retry retry) {
        this.webClient = builder
            .baseUrl(Objects.requireNonNullElse(baseUrl, BASE_URL))
            .build();
        this.retry = retry;
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
