package edu.java.configuration.access;

import edu.java.clients.BotClient;
import edu.java.clients.GitHubClient;
import edu.java.clients.StackoverflowClient;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.repository.jpa.JpaLinkRepositoryInterface;
import edu.java.services.ChatService;
import edu.java.services.LinkService;
import edu.java.services.jpa.JpaChatService;
import edu.java.services.jpa.JpaLinkService;
import edu.java.updater.LinkUpdater;
import edu.java.utils.LinkProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {

    @Bean
    public LinkService linkService(
        LinkProcessor linkValidator,
        JpaLinkRepositoryInterface linkRepository,
        JpaChatRepository chatRepository
    ) {
        return new JpaLinkService(
            linkValidator,
            linkRepository,
            chatRepository
        );
    }

    @Bean
    public ChatService chatService(JpaChatRepository chatRepository) {
        return new JpaChatService(chatRepository);
    }

    @Bean
    public LinkUpdater linkUpdater(
        JpaLinkRepository linkRepository,
        BotClient botClient,
        LinkProcessor linkProcessor,
        GitHubClient gitHubClient,
        StackoverflowClient stackoverflowClient
    ) {
        return new LinkUpdater(
            linkRepository,
            botClient,
            linkProcessor,
            gitHubClient,
            stackoverflowClient
        );
    }
}
