package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import edu.java.clients.BotClient;
import edu.java.configuration.RetryConfiguration;
import edu.java.models.LinkUpdateRequest;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.GATEWAY_TIMEOUT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@WireMockTest(httpPort = 8090)
public class BotClientTest {
    private final RetryConfiguration retryConfiguration = new RetryConfiguration(
        5000,
        3,
        List.of(INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERVICE_UNAVAILABLE, GATEWAY_TIMEOUT)
    );

    private final BotClient botClient =
        new BotClient(WebClient.builder(), "http://localhost:8090", retryConfiguration.constant());

    @Test
    public void testSendUpdate() {
        stubFor(post(urlEqualTo("/updates"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("Update processed")));
        String response = botClient.sendUpdate(new LinkUpdateRequest(1L, URI.create("1"), "1", List.of(1L)));
        assertThat(response).isEqualTo("Update processed");
    }

    @Test
    public void testSendUpdateWithRetry() {
        stubFor(post(urlEqualTo("/updates"))
            .inScenario("Retry scenario")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(aResponse()
                .withStatus(500)
                .withHeader("Content-Type", "application/json")
                .withBody("Update processed"))
            .willSetStateTo("Retry once"));

        stubFor(post(urlEqualTo("/updates"))
            .inScenario("Retry scenario")
            .whenScenarioStateIs("Retry once")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("Update processed")));

        String response = botClient.sendUpdate(new LinkUpdateRequest(1L, URI.create("1"), "1", List.of(1L)));
        assertThat(response).isEqualTo("Update processed");
        verify(2, postRequestedFor(urlEqualTo("/updates")));
    }
}
