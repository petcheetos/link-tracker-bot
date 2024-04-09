package edu.java.bot.clients;

import edu.java.bot.configuration.retry.RetryPolicyConfig;
import edu.java.bot.exceptions.ApiErrorException;
import edu.java.models.AddLinkRequest;
import edu.java.models.ApiErrorResponse;
import edu.java.models.LinkResponse;
import edu.java.models.ListLinkResponse;
import edu.java.models.RemoveLinkRequest;
import io.github.resilience4j.retry.Retry;
import jakarta.annotation.PostConstruct;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RetryPolicyConfig retryPolicyConfig;

    private Retry retry;

    public ScrapperClient(WebClient.Builder builder, String baseUrl) {
        this.webClient = builder
            .baseUrl(Objects.requireNonNullElse(baseUrl, BASE_URL))
            .build();
    }

    @PostConstruct
    private void initRetry() {
        retry = retryPolicyConfig.configure();
    }

    public String registerChat(long id) {
        return retry.executeSupplier(() -> webClient
            .post()
            .uri(uriBuilder -> uriBuilder.path("tg-chat/{id}").build(id))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> clientResponse
                .bodyToMono(ApiErrorResponse.class)
                .flatMap(apiErrorResponse -> Mono.error(new ApiErrorException(apiErrorResponse))))
            .bodyToMono(String.class)
            .block());
    }

    public String deleteChat(long id) {
        return retry.executeSupplier(() -> webClient
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
            .block());
    }

    public ListLinkResponse getLinks(long id) {
        return retry.executeSupplier(() -> webClient
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
            .block());
    }

    public LinkResponse addLink(long id, AddLinkRequest addLinkRequest) {
        return retry.executeSupplier(() -> webClient.post()
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
            .block());
    }

    public LinkResponse deleteLink(long id, RemoveLinkRequest removeLinkRequest) {
        return retry.executeSupplier(() -> webClient
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
            .block());
    }
}
