package edu.java.client;

import edu.java.dto.GitHubResponse;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClient {
    private final WebClient webClient;

    public GitHubClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public GitHubResponse getRepositoryInfo(String owner, String repo) {
        return webClient.get()
            .uri(owner + "/" + repo)
            .retrieve()
            .bodyToMono(GitHubResponse.class)
            .block();
    }
}
