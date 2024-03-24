package service;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.configuration.CommandConfig;
import edu.java.bot.services.BotCommandService;
import edu.java.bot.services.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CommandServiceTest {

    private final ScrapperClient scrapperClient = new ScrapperClient(WebClient.builder(), "http://localhost:8080");
    private final ClientService clientService = new ClientService(scrapperClient);
    @Mock
    CommandConfig commandConfig = new CommandConfig();

    @InjectMocks
    BotCommandService botCommandService = new BotCommandService(commandConfig);

    @Test
    public void createResponseWithUnknownCommand() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().text()).thenReturn("/unknown");
        String msg = botCommandService.createResponse(update, clientService);
        assertThat(msg).isEqualTo("I do not know this command :(");
    }

    @Test
    public void createResponseWithTrackCommandWithEmptyLink() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().text()).thenReturn("/track");
        String msg = botCommandService.createResponse(update, clientService);
        assertThat(msg).isEqualTo("Empty link! Type /track link");
    }

    @Test
    public void createResponseWithUntrackCommandWithEmptyLink() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().text()).thenReturn("/untrack");
        String msg = botCommandService.createResponse(update, clientService);
        assertThat(msg).isEqualTo("Empty link! Type /untrack link");
    }

    @Test
    public void createResponseWithHelpCommand() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().text()).thenReturn("/help");
        String msg = botCommandService.createResponse(update, clientService);
        assertThat(msg).isEqualTo("""
            Commands:
            \t- /start - Register a user
            \t- /list - Send a list of tracking sites
            \t- /track - Add link for tracking
            \t- /untrack - Remove a site from the tracking list
            """);
    }
}
