package edu.java.clients;

import edu.java.dto.StackoverflowResponse;
import org.springframework.web.reactive.function.client.WebClient;

public class StackoverflowClient {
    private final WebClient webClient;

    public StackoverflowClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
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
