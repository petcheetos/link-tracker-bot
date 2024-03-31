package edu.java.clients;

import edu.java.configuration.RetryPolicyConfig;
import edu.java.exception.ApiErrorException;
import edu.java.models.ApiErrorResponse;
import edu.java.models.LinkUpdateRequest;
import edu.java.retry_model.RetryPolicy;
import edu.java.retry_model.RetryPolicyData;
import io.github.resilience4j.retry.Retry;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClient {
    private final static String BASE_URL = "http://localhost:8090";
    private final WebClient webClient;

    private Retry retry;

    @Value(value = "${api.bot.retry-policy}")
    private RetryPolicy retryPolicy;

    @Value(value = "${api.bot.attempts}")
    private int attempts;

    @Value("#{'${api.bot.http-statuses}'.split(',')}")
    private List<HttpStatus> httpStatuses;

    public BotClient(WebClient.Builder builder, String baseUrl) {
        this.webClient = builder.baseUrl(Objects.requireNonNullElse(baseUrl, BASE_URL)).build();
    }

    @PostConstruct
    private void initRetry() {
        RetryPolicyData retryPolicyData = new RetryPolicyData();
        retryPolicyData.setRetryPolicy(retryPolicy);
        retryPolicyData.setAttempts(attempts);
        retryPolicyData.setHttpStatuses(httpStatuses);
        retry = RetryPolicyConfig.config(retryPolicyData);
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

    public String retrySendUpdate(LinkUpdateRequest request) {
        return Retry.decorateSupplier(retry, () -> sendUpdate(request)).get();
    }
}
