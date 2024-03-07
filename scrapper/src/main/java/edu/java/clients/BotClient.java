package edu.java.clients;

import edu.java.exception.ApiErrorException;
import edu.java.models.ApiErrorResponse;
import edu.java.models.LinkUpdateRequest;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClient {
    private final static String BASE_URL = "http://localhost:8090";
    private final WebClient webClient;

    public BotClient(WebClient.Builder builder, String baseUrl) {
        this.webClient = builder.baseUrl(Objects.requireNonNullElse(baseUrl, BASE_URL)).build();
    }

    public String sendUpdate(LinkUpdateRequest request) {
        return webClient
            .post()
            .uri("/updates")
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> clientResponse
                .bodyToMono(ApiErrorResponse.class)
                .flatMap(apiErrorResponse -> Mono.error(new ApiErrorException(apiErrorResponse))))
            .bodyToMono(String.class)
            .block();
    }
}
