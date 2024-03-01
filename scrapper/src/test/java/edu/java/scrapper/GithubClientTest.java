package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.clients.GitHubClient;
import edu.java.dto.GitHubResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8080)
public class GithubClientTest {
    @Autowired
    WebClient.Builder webClientBuilder;

    private final GitHubClient githubClient = new GitHubClient("http://localhost:8080/repos/");

    @Test
    void getRepositoryInfo() {
        stubFor(get(urlPathMatching("/repos/petcheetos/backend-java-course"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "updated_at": "2024-02-03T13:12:41Z",
                        "pushed_at": "2024-02-23T17:30:12Z",
                        "created_at": "2024-02-03T13:08:55Z"
                    }
                    """)));
        GitHubResponse gitHubResponse = githubClient.getRepositoryInfo("petcheetos", "backend-java-course");
        assertThat(gitHubResponse.updatedAt()).isEqualTo("2024-02-03T13:12:41Z");
        assertThat(gitHubResponse.createdAt()).isEqualTo("2024-02-03T13:08:55Z");
        assertThat(gitHubResponse.pushedAt()).isEqualTo("2024-02-23T17:30:12Z");
    }
}
