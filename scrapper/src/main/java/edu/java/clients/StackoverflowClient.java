package edu.java.clients;

import edu.java.dto.StackoverflowResponse;
import java.util.Objects;
import org.springframework.web.reactive.function.client.WebClient;

public class StackoverflowClient {
    private static final String BASE_URL = "https://api.stackexchange.com/2.3";
    private final WebClient webClient;

    public StackoverflowClient(WebClient.Builder builder, String baseUrl) {
        this.webClient = builder
            .baseUrl(Objects.requireNonNullElse(baseUrl, BASE_URL))
            .build();
    }

    public StackoverflowResponse getUpdate(String id) {
        return webClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path(id + "/")
                .queryParam("sort", "activity")
                .queryParam("site", "stackoverflow")
                .queryParam("order", "desc")
                .queryParam("filter", "withbody")
                .build())
            .retrieve()
            .bodyToMono(StackoverflowResponse.class)
            .block();
    }
}
