package commands;

import edu.java.bot.commands.Command;
import edu.java.bot.commands.UntrackCommand;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UntrackCommandTest {
    private final Command untrack = new UntrackCommand();

    @Test
    void testStartName() {
        assertThat(untrack.name()).isNotBlank().contains("/untrack");
    }

    @Test
    void testStartDescription() {
        assertThat(untrack.description()).isNotBlank();
    }
}
