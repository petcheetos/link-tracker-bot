package edu.java.bot.clients;

import edu.java.bot.configuration.ScrapperRetryPolicyConfig;
import edu.java.bot.exceptions.ApiErrorException;
import edu.java.bot.retry_model.ScrapperRetryPolicy;
import edu.java.bot.retry_model.ScrapperRetryPolicyData;
import edu.java.models.AddLinkRequest;
import edu.java.models.ApiErrorResponse;
import edu.java.models.LinkResponse;
import edu.java.models.ListLinkResponse;
import edu.java.models.RemoveLinkRequest;
import io.github.resilience4j.retry.Retry;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperClient {
    private static final String LINKS = "/links";
    private static final String TG_CHAT_ID = "Tg-Chat-Id";
    private static final String BASE_URL = "http://localhost:8080";
    private final WebClient webClient;

    private Retry retry;

    @Value(value = "${api.scrapper.retry-policy}")
    private ScrapperRetryPolicy retryPolicy;

    @Value(value = "${api.scrapper.attempts}")
    private int attempts;

    @Value("#{'${api.scrapper.http-statuses}'.split(',')}")
    private List<HttpStatus> httpStatuses;

    public ScrapperClient(WebClient.Builder builder, String baseUrl) {
        this.webClient = builder
            .baseUrl(Objects.requireNonNullElse(baseUrl, BASE_URL))
            .build();
    }

    @PostConstruct
    private void initRetry() {
        ScrapperRetryPolicyData retryPolicyData = new ScrapperRetryPolicyData();
        retryPolicyData.setRetryPolicy(retryPolicy);
        retryPolicyData.setAttempts(attempts);
        retryPolicyData.setHttpStatuses(httpStatuses);
        retry = ScrapperRetryPolicyConfig.config(retryPolicyData);
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

    public String retryRegisterChat(long id) {
        return Retry.decorateSupplier(retry, () -> registerChat(id)).get();
    }

    public String retryDeleteChat(long id) {
        return Retry.decorateSupplier(retry, () -> deleteChat(id)).get();
    }

    public ListLinkResponse retryGetLinks(long id) {
        return Retry.decorateSupplier(retry, () -> getLinks(id)).get();
    }

    public LinkResponse retryAddLink(long id, AddLinkRequest addLinkRequest) {
        return Retry.decorateSupplier(retry, () -> addLink(id, addLinkRequest)).get();
    }

    public LinkResponse retryDeleteLink(long id, RemoveLinkRequest removeLinkRequest) {
        return Retry.decorateSupplier(retry, () -> deleteLink(id, removeLinkRequest)).get();
    }
}
