package edu.java.configuration.access;

import edu.java.clients.BotClient;
import edu.java.clients.GitHubClient;
import edu.java.clients.StackoverflowClient;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.services.ChatService;
import edu.java.services.LinkService;
import edu.java.services.jdbc.JdbcChatService;
import edu.java.services.jdbc.JdbcLinkService;
import edu.java.updater.LinkUpdater;
import edu.java.utils.LinkProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {

    @Bean
    public LinkService linkService(
        JdbcChatRepository chatRepository,
        JdbcLinkRepository linkRepository,
        LinkProcessor linkValidator
    ) {
        return new JdbcLinkService(
            chatRepository,
            linkRepository,
            linkValidator
        );
    }

    @Bean
    public ChatService chatService(JdbcChatRepository chatRepository) {
        return new JdbcChatService(chatRepository);
    }

    @Bean
    public LinkUpdater linkUpdater(
        JdbcLinkRepository linkRepository,
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
