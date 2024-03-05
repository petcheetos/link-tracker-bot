package edu.java.clients;

import edu.java.dto.GitHubResponse;
import java.util.Objects;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClient {
    private static final String BASE_URL = "https://api.github.com";
    private final WebClient webClient;

    public GitHubClient(WebClient.Builder builder, String baseUrl) {
        this.webClient = builder
            .baseUrl(Objects.requireNonNullElse(baseUrl, BASE_URL))
            .build();
    }

    public GitHubResponse getRepositoryInfo(String owner, String repo) {
        return webClient.get()
            .uri(owner + "/" + repo)
            .retrieve()
            .bodyToMono(GitHubResponse.class)
            .block();
    }
}
