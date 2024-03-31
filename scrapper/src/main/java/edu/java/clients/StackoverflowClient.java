package edu.java.clients;

import edu.java.configuration.RetryPolicyConfig;
import edu.java.dto.StackoverflowResponse;
import edu.java.retry_model.RetryPolicy;
import edu.java.retry_model.RetryPolicyData;
import io.github.resilience4j.retry.Retry;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

public class StackoverflowClient {
    private static final String BASE_URL = "https://api.stackexchange.com/2.3";
    private final WebClient webClient;

    private Retry retry;

    @Value(value = "${api.stackoverflow.retry-policy}")
    private RetryPolicy retryPolicy;

    @Value(value = "${api.stackoverflow.attempts}")
    private int attempts;

    @Value("#{'${api.stackoverflow.http-statuses}'.split(',')}")
    private List<HttpStatus> httpStatuses;

    public StackoverflowClient(WebClient.Builder builder, String baseUrl) {
        this.webClient = builder
            .baseUrl(Objects.requireNonNullElse(baseUrl, BASE_URL))
            .build();
    }

    @PostConstruct
    private void initRetry() {
        RetryPolicyData retryPolicyData = new RetryPolicyData();
        retryPolicyData.setRetryPolicy(retryPolicy);
        retryPolicyData.setAttempts(attempts);
        retryPolicyData.setHttpStatuses(httpStatuses);
        retry = RetryPolicyConfig.config(retryPolicyData);
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

    public StackoverflowResponse retryGetUpdate(String id) {
        return Retry.decorateSupplier(retry, () -> getUpdate(id)).get();
    }
}
