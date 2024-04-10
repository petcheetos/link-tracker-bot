package edu.java.bot.configuration;

import edu.java.bot.clients.ScrapperClient;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {
    private final WebClient.Builder builder;

    @Value("${app.scrapper-base-url.base-url:}")
    private String baseUrl;

    @Bean
    public ScrapperClient scrapperClient(Retry retry) {
        return new ScrapperClient(builder, baseUrl, retry);
    }
}
