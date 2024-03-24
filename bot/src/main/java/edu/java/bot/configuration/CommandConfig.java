package edu.java.bot.configuration;

import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("HideUtilityClassConstructor")
@Configuration
@RequiredArgsConstructor
public class CommandConfig {

    @Bean
    public Map<String, Command> commandMap() {
        Map<String, Command> commandMap = new HashMap<>();
        Command start = new StartCommand();
        Command list = new ListCommand();
        Command track = new TrackCommand();
        Command untrack = new UntrackCommand();
        Command help = new HelpCommand(List.of(start, list, track, untrack));
        commandMap.put("/start", start);
        commandMap.put("/list", list);
        commandMap.put("/track", track);
        commandMap.put("/untrack", untrack);
        commandMap.put("/help", help);
        return commandMap;
    }
}
