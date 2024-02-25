package edu.java.configuration;

import edu.java.client.GitHubClient;
import edu.java.client.StackoverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Bean
    public GitHubClient gitHubClient(ApplicationConfig applicationConfig) {
        return new GitHubClient(applicationConfig.githubBaseUrl().baseUrl());
    }

    @Bean
    public StackoverflowClient stackoverflowClient(ApplicationConfig applicationConfig) {
        return new StackoverflowClient(applicationConfig.stackoverflowBaseUrl().baseUrl());
    }
}
