import com.pengrad.telegrambot.model.Update;
import edu.java.bot.configuration.CommandConfig;
import edu.java.bot.service.BotCommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CommandServiceTest {

    @Mock
    CommandConfig commandConfig = new CommandConfig();

    @InjectMocks
    BotCommandService botCommandService = new BotCommandService(commandConfig);

    @Test
    public void createResponseWithUnknownCommand() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().text()).thenReturn("/unknown");
        String msg = botCommandService.createResponse(update);
        assertThat(msg).isEqualTo("I don't know this command! Type /help to find out about my commands");
    }

    @Test
    public void createResponseWithStartCommand() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().text()).thenReturn("/start");
        String msg = botCommandService.createResponse(update);
        assertThat(msg).isEqualTo("Hi! I am a link tracking bot! Type /help to see the list of available commands!");
    }

    @Test
    public void createResponseWithListCommand() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().text()).thenReturn("/list");
        String msg = botCommandService.createResponse(update);
        assertThat(msg).isEqualTo("List of tracking sites:");
    }

    @Test
    public void createResponseWithTrackCommand() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().text()).thenReturn("/track");
        String msg = botCommandService.createResponse(update);
        assertThat(msg).isEqualTo("What site do you want to track updates to? Please, send a link!");
    }

    @Test
    public void createResponseWithUntrackCommand() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().text()).thenReturn("/untrack");
        String msg = botCommandService.createResponse(update);
        assertThat(msg).isEqualTo("What site do you want to stop receiving updates from? Please, send a link!");
    }

    @Test
    public void createResponseWithHelpCommand() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().text()).thenReturn("/help");
        String msg = botCommandService.createResponse(update);
        assertThat(msg).isEqualTo("""
                Commands:
                \t- /start - Register a user
                \t- /list - Send a list of tracking sites
                \t- /track - Add link for tracking
                \t- /untrack - Remove a site from the tracking list
                """);
    }
}
