package clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.configuration.RetryConfiguration;
import edu.java.bot.exceptions.ApiErrorException;
import edu.java.models.AddLinkRequest;
import edu.java.models.LinkResponse;
import edu.java.models.ListLinkResponse;
import edu.java.models.RemoveLinkRequest;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.GATEWAY_TIMEOUT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@WireMockTest(httpPort = 8080)
public class ScrapperClientTest {

    private final RetryConfiguration retryConfiguration = new RetryConfiguration(
        5000,
        3,
        List.of(INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERVICE_UNAVAILABLE, GATEWAY_TIMEOUT)
    );

    private final ScrapperClient scrapperClient =
        new ScrapperClient(WebClient.builder(), "http://localhost:8080", retryConfiguration.constant());

    private final static String INVALID_BODY = """
            {
                "description":"error",
                "code":"400",
                "exceptionName":"error",
                "exceptionMessage":"error",
                "stackTrace":[
                    "error",
                    "error",
                    "error"
                ]
            }
        """;

    private final static String NOT_FOUND = """
            {
                "description":"error",
                "code":"404",
                "exceptionName":"error",
                "exceptionMessage":"error",
                "stackTrace":[
                    "error",
                    "error",
                    "error"
                ]
            }
        """;

    @Test
    public void testRegisterChat() {
        stubFor(post(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("Chat registered")));
        String response = scrapperClient.registerChat(1L);
        assertThat(response).isEqualTo("Chat registered");
    }

    @Test
    public void testRegisterChatWithError() {
        stubFor(post(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("Chat registered")));
        scrapperClient.registerChat(1L);

        stubFor(post(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader("Content-Type", "application/json")
                .withBody(INVALID_BODY)));

        assertThrows(ApiErrorException.class, () -> scrapperClient.registerChat(1L));
    }

    @Test
    public void testDeleteChat() {
        stubFor(delete(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("Chat deleted")));
        String response = scrapperClient.deleteChat(1L);
        assertThat(response).isEqualTo("Chat deleted");
    }

    @Test
    public void testDeleteChatWithError() {
        stubFor(delete(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody(NOT_FOUND)));
        assertThrows(ApiErrorException.class, () -> scrapperClient.deleteChat(1L));
    }

    @Test
    public void testGetLinks() {
        stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                     {
                         "links":[
                             {
                                 "id":1,
                                 "url":"link"
                             }
                         ],
                         "size":1
                     }
                    """)));
        ListLinkResponse response = scrapperClient.getLinks(1L);
        assertThat(response.size()).isEqualTo(1);
    }

    @Test
    public void testGetLinksWithError() {
        stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody(NOT_FOUND)));
        assertThrows(ApiErrorException.class, () -> scrapperClient.getLinks(1L));

        stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("-1"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader("Content-Type", "application/json")
                .withBody(INVALID_BODY)));
        assertThrows(ApiErrorException.class, () -> scrapperClient.getLinks(1L));
    }

    @Test
    public void testAddLink() {
        stubFor(post(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "id":1,
                        "url":"1"
                    }
                    """)));
        LinkResponse response = scrapperClient.addLink(1L, new AddLinkRequest(URI.create("1")));
        assertThat(response.url()).isEqualTo(URI.create("1"));
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    public void testAddLinkWithError() {
        stubFor(post(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody(NOT_FOUND)));
        assertThrows(ApiErrorException.class, () -> scrapperClient.addLink(1L, new AddLinkRequest(URI.create("1"))));
    }

    @Test
    public void testDeleteLink() {
        stubFor(delete(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "id":1,
                        "url":"1"
                    }
                    """)));
        LinkResponse response = scrapperClient.deleteLink(1L, new RemoveLinkRequest(URI.create("1")));
        assertThat(response.url().getPath()).isEqualTo("1");
        assertThat(response.id()).isEqualTo(1);
    }

    @Test
    public void testDeleteLinkWithError() {
        stubFor(delete(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody(NOT_FOUND)));
        assertThrows(
            ApiErrorException.class,
            () -> scrapperClient.deleteLink(1L, new RemoveLinkRequest(URI.create("1")))
        );

        stubFor(delete(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader("Content-Type", "application/json")
                .withBody(INVALID_BODY)));
        assertThrows(
            ApiErrorException.class,
            () -> scrapperClient.deleteLink(1L, new RemoveLinkRequest(URI.create("1")))
        );
    }

    @Test
    @DisplayName("Testing retry")
    public void testRegisterChatWithRetryWhenServerError() {
        stubFor(post(urlEqualTo("/tg-chat/1"))
            .inScenario("Retry scenario")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(aResponse()
                .withStatus(500))
            .willSetStateTo("Retry once"));

        stubFor(post(urlEqualTo("/tg-chat/1"))
            .inScenario("Retry scenario")
            .whenScenarioStateIs("Retry once")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("Chat registered")));

        String response = scrapperClient.registerChat(1L);
        assertThat(response).isEqualTo("Chat registered");
        verify(2, postRequestedFor(urlEqualTo("/tg-chat/1")));
    }

    @Test
    @DisplayName("Testing retry")
    public void testDeleteChatWithRetryWhenBadGateway() {
        stubFor(delete(urlEqualTo("/tg-chat/1"))
            .inScenario("Retry scenario for delete")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(aResponse()
                .withStatus(502))
            .willSetStateTo("Retry once for delete"));

        stubFor(delete(urlEqualTo("/tg-chat/1"))
            .inScenario("Retry scenario for delete")
            .whenScenarioStateIs("Retry once for delete")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("Chat deleted")));

        String response = scrapperClient.deleteChat(1L);
        assertThat(response).isEqualTo("Chat deleted");
        verify(2, deleteRequestedFor(urlEqualTo("/tg-chat/1")));
    }

    @Test
    @DisplayName("Testing retry")
    public void testGetLinksWithRetryWhenServiceUnavailable() {
        stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .inScenario("Retry scenario for get")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(aResponse()
                .withStatus(503))
            .willSetStateTo("Retry once for get"));

        stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .inScenario("Retry scenario for get")
            .whenScenarioStateIs("Retry once for get")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                     {
                         "links":[
                             {
                                 "id":1,
                                 "url":"link"
                             }
                         ],
                         "size":1
                     }
                    """)));

        ListLinkResponse response = scrapperClient.getLinks(1L);
        assertThat(response.size()).isEqualTo(1);
        verify(2, getRequestedFor(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1")));
    }
}
