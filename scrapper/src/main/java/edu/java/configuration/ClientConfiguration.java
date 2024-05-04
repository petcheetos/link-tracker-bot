package edu.java.configuration;

import edu.java.clients.BotClient;
import edu.java.clients.GitHubClient;
import edu.java.clients.StackoverflowClient;
import io.github.resilience4j.retry.Retry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {
    private final WebClient.Builder builder;

    public ClientConfiguration(WebClient.Builder builder) {
        this.builder = builder;
    }

    @Bean
    public GitHubClient gitHubClient(ApplicationConfig applicationConfig, Retry retry) {
        return new GitHubClient(builder, applicationConfig.githubBaseUrl().baseUrl(), retry);
    }

    @Bean
    public StackoverflowClient stackoverflowClient(ApplicationConfig applicationConfig, Retry retry) {
        return new StackoverflowClient(builder, applicationConfig.stackoverflowBaseUrl().baseUrl(), retry);
    }

    @Bean
    public BotClient botClient(ApplicationConfig applicationConfig, Retry retry) {
        return new BotClient(builder, applicationConfig.botBaseUrl().baseUrl(), retry);
    }
}
