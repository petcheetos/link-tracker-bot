package commands;

import edu.java.bot.commands.Command;
import edu.java.bot.commands.TrackCommand;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TrackCommandTest {
    private final Command track = new TrackCommand();

    @Test
    void testStartName() {
        assertThat(track.name()).isNotBlank().contains("/track");
    }

    @Test
    void testStartDescription() {
        assertThat(track.description()).isNotBlank();
    }
}
