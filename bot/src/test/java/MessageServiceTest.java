import com.pengrad.telegrambot.model.Update;
import edu.java.bot.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Test
    public void createResponseWithUnknownCommand() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().text()).thenReturn("/unknown");
        MessageService messageService = new MessageService();
        String msg = messageService.createResponse(update);
        assertThat(msg).isEqualTo("I don't know this command! Type /help to find out about my commands");
    }

    @Test
    public void createResponseWithStartCommand() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().text()).thenReturn("/start");
        MessageService messageService = new MessageService();
        String msg = messageService.createResponse(update);
        assertThat(msg).isEqualTo("Hi! I am a link tracking bot! Type /help to see the list of available commands!");
    }

    @Test
    public void createResponseWithListCommand() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().text()).thenReturn("/list");
        MessageService messageService = new MessageService();
        String msg = messageService.createResponse(update);
        assertThat(msg).isEqualTo("List of tracking sites:");
    }

    @Test
    public void createResponseWithTrackCommand() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().text()).thenReturn("/track");
        MessageService messageService = new MessageService();
        String msg = messageService.createResponse(update);
        assertThat(msg).isEqualTo("What site do you want to track updates to? Please, send a link!");
    }

    @Test
    public void createResponseWithUntrackCommand() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().text()).thenReturn("/untrack");
        MessageService messageService = new MessageService();
        String msg = messageService.createResponse(update);
        assertThat(msg).isEqualTo("What site do you want to stop receiving updates from? Please, send a link!");
    }

    @Test
    public void createResponseWithHelpCommand() {
        Update update = Mockito.mock(Update.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(update.message().text()).thenReturn("/help");
        MessageService messageService = new MessageService();
        String msg = messageService.createResponse(update);
        assertThat(msg).isEqualTo("Commands:\n" +
            "\t- /start - Register a user\n" +
            "\t- /list - Send a list of tracking sites\n" +
            "\t- /track - Add link for tracking\n" +
            "\t- /untrack - Remove a site from the tracking list\n");
    }
}
