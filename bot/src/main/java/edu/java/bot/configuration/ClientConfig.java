package edu.java.bot.configuration;

import edu.java.bot.clients.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {
    private final WebClient.Builder builder;

    @Bean
    public ScrapperClient scrapperClient(ApplicationConfig applicationConfig) {
        return new ScrapperClient(builder, applicationConfig.scrapperBaseUrl().baseUrl());
    }
}
