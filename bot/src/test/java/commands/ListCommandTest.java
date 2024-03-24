package commands;

import edu.java.bot.commands.Command;
import edu.java.bot.commands.ListCommand;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ListCommandTest {
    private final Command list = new ListCommand();

    @Test
    void testListName() {
        assertThat(list.name()).isNotBlank().contains("/list");
    }

    @Test
    void testListDescription() {
        assertThat(list.description()).isNotBlank();
    }
}
