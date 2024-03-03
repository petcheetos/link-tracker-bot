package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.clients.BotClient;
import edu.java.models.LinkUpdateRequest;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8090)
public class BotClientTest {

    private final BotClient botClient = new BotClient(WebClient.builder(), "http://localhost:8090");

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
}
