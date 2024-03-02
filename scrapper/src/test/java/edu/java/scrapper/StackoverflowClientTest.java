package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.clients.StackoverflowClient;
import edu.java.dto.StackoverflowResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8080)
public class StackoverflowClientTest {

    private final StackoverflowClient stackoverflowClient =
        new StackoverflowClient(WebClient.builder(), "http://localhost:8080/2.3/questions/");

    private final String response =
        """
            {
                "items": [
                    {
                        "tags": [
                            "java",
                            "hibernate",
                            "annotations"
                        ],
                        "owner": {
                            "account_id": 48,
                            "reputation": 6197,
                            "user_id": 59,
                            "user_type": "registered",
                            "accept_rate": 79,
                            "profile_image": "https://www.gravatar.com/avatar/3d5932182385c3cceb7b48a6ebf25d83?s=256&d=identicon&r=PG",
                            "display_name": "saint_groceon",
                            "link": "https://stackoverflow.com/users/59/saint-groceon"
                        },
                        "is_answered": false,
                        "view_count": 14,
                        "answer_count": 0,
                        "score": 4,
                        "last_activity_date": 1708778910,
                        "creation_date": 1708778910,
                        "question_id": 78052463,
                        "content_license": "CC BY-SA 4.0"
                    }
                ],
                "has_more": false,
                "quota_max": 300,
                "quota_remaining": 296
            }""";

    @Test
    void getUpdate() {
        stubFor(get(urlPathMatching("/2\\.3/questions/78052463/"))
            .withQueryParam("site", equalTo("stackoverflow"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(response)));
        StackoverflowResponse stackoverflowResponse = stackoverflowClient.getUpdate("78052463");
        System.out.println(stackoverflowResponse);
        assertThat(stackoverflowResponse.items().getFirst().answerCount()).isEqualTo(0);
        assertThat(stackoverflowResponse.items().getFirst().questionId()).isEqualTo(78052463);
    }
}
