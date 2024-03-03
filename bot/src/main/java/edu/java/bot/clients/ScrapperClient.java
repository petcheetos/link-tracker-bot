package edu.java.bot.clients;

import edu.java.bot.exceptions.ApiErrorException;
import edu.java.models.AddLinkRequest;
import edu.java.models.ApiErrorResponse;
import edu.java.models.LinkResponse;
import edu.java.models.ListLinkResponse;
import edu.java.models.RemoveLinkRequest;
import java.util.Objects;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperClient {
    private static final String LINKS = "/links";
    private static final String TG_CHAT_ID = "Tg-Chat-Id";
    private static final String BASE_URL = "http://localhost:8080";
    private final WebClient webClient;

    public ScrapperClient(WebClient.Builder builder, String baseUrl) {
        this.webClient = builder
            .baseUrl(Objects.requireNonNullElse(baseUrl, BASE_URL))
            .build();
    }

    public String registerChat(long id) {
        return webClient
            .post()
            .uri(uriBuilder -> uriBuilder.path("tg-chat/{id}").build(id))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> clientResponse
                .bodyToMono(ApiErrorResponse.class)
                .flatMap(apiErrorResponse -> Mono.error(new ApiErrorException(apiErrorResponse))))
            .bodyToMono(String.class)
            .block();
    }

    public String deleteChat(long id) {
        return webClient
            .delete()
            .uri(uriBuilder -> uriBuilder.path("/tg-chat/{id}").build(id))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                clientResponse -> clientResponse
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(apiErrorResponse -> Mono.error(new ApiErrorException(apiErrorResponse)))
            )
            .bodyToMono(String.class)
            .block();
    }

    public ListLinkResponse getLinks(long id) {
        return webClient
            .get()
            .uri(LINKS)
            .header(TG_CHAT_ID, String.valueOf(id))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                clientResponse -> clientResponse
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(apiErrorResponse -> Mono.error(new ApiErrorException(apiErrorResponse)))
            )
            .bodyToMono(ListLinkResponse.class)
            .block();
    }

    public LinkResponse addLink(long id, AddLinkRequest addLinkRequest) {
        return webClient
            .post()
            .uri(LINKS)
            .header(TG_CHAT_ID, String.valueOf(id))
            .body(BodyInserters.fromValue(addLinkRequest))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                clientResponse -> clientResponse
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(apiErrorResponse -> Mono.error(new ApiErrorException(apiErrorResponse)))
            )
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse deleteLink(long id, RemoveLinkRequest removeLinkRequest) {
        return webClient
            .method(HttpMethod.DELETE)
            .uri(LINKS)
            .header(TG_CHAT_ID, String.valueOf(id))
            .body(BodyInserters.fromValue(removeLinkRequest))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                clientResponse -> clientResponse
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(apiErrorResponse -> Mono.error(new ApiErrorException(apiErrorResponse)))
            )
            .bodyToMono(LinkResponse.class)
            .block();
    }
}
