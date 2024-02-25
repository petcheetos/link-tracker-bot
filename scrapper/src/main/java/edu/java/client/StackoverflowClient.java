package edu.java.client;

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
            .uri(id + "/?sort=activity&site=stackoverflow&order=desc&filter=withbody")
            .retrieve()
            .bodyToMono(StackoverflowResponse.class)
            .block();
    }
}
