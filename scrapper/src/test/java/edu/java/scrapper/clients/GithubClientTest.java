package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import edu.java.clients.GitHubClient;
import edu.java.configuration.RetryConfiguration;
import edu.java.dto.GitHubResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.GATEWAY_TIMEOUT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@WireMockTest(httpPort = 8080)
public class GithubClientTest {
    private final RetryConfiguration retryConfiguration = new RetryConfiguration(
        5000,
        3,
        List.of(INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERVICE_UNAVAILABLE, GATEWAY_TIMEOUT)
    );
    private final GitHubClient githubClient =
        new GitHubClient(WebClient.builder(), "http://localhost:8080/repos/", retryConfiguration.exponential());

    private final String body = """
                    {
                        "updated_at": "2024-02-03T13:12:41Z",
                        "pushed_at": "2024-02-23T17:30:12Z",
                        "created_at": "2024-02-03T13:08:55Z"
                    }
                    """;
    @Test
    void getRepositoryInfo() {
        stubFor(get(urlPathMatching("/repos/petcheetos/backend-java-course"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(body)));
        GitHubResponse gitHubResponse = githubClient.getRepositoryInfo("petcheetos", "backend-java-course");
        assertThat(gitHubResponse.updatedAt()).isEqualTo("2024-02-03T13:12:41Z");
        assertThat(gitHubResponse.createdAt()).isEqualTo("2024-02-03T13:08:55Z");
        assertThat(gitHubResponse.pushedAt()).isEqualTo("2024-02-23T17:30:12Z");
    }

    @Test
    void testRetry() {
        stubFor(get(urlPathMatching("/repos/petcheetos/backend-java-course"))
            .inScenario("Retry scenario for get")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(aResponse()
                .withStatus(500))
            .willSetStateTo("Retry once for get"));

        stubFor(get(urlPathMatching("/repos/petcheetos/backend-java-course"))
            .inScenario("Retry scenario for get")
            .whenScenarioStateIs("Retry once for get")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(body)));
        GitHubResponse gitHubResponse = githubClient.getRepositoryInfo("petcheetos", "backend-java-course");
        assertThat(gitHubResponse.updatedAt()).isEqualTo("2024-02-03T13:12:41Z");
        assertThat(gitHubResponse.createdAt()).isEqualTo("2024-02-03T13:08:55Z");
        assertThat(gitHubResponse.pushedAt()).isEqualTo("2024-02-23T17:30:12Z");
    }
}
