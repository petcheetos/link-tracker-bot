package commands;

import edu.java.bot.commands.Command;
import edu.java.bot.commands.StartCommand;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class StartCommandTest {
    private final Command start = new StartCommand();

    @Test
    void testStartName() {
        assertThat(start.name()).isNotBlank().contains("/start");
    }

    @Test
    void testStartDescription() {
        assertThat(start.description()).isNotBlank();
    }
}
