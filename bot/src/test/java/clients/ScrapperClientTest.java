package clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.exceptions.ApiErrorException;
import edu.java.models.AddLinkRequest;
import edu.java.models.LinkResponse;
import edu.java.models.ListLinkResponse;
import edu.java.models.RemoveLinkRequest;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpPort = 8080)
public class ScrapperClientTest {

    private final ScrapperClient scrapperClient = new ScrapperClient(WebClient.builder(), "http://localhost:8080");

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
        String response = scrapperClient.registerChat(1L);

        stubFor(post(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader("Content-Type", "application/json")
                .withBody(INVALID_BODY)));

        assertThrows(ApiErrorException.class, () -> scrapperClient.registerChat(1L));
    }

    @Test
    public void testDeleteChat() {
        stubFor(post(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("Chat deleted")));
        String response = scrapperClient.registerChat(1L);
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
}
