package edu.java.bot.services;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.Command;
import edu.java.bot.configuration.CommandConfig;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static edu.java.bot.configuration.MessageConstants.UNKNOWN_COMMAND;

@Service
public class BotCommandService implements CommandService {
    private final Map<String, Command> commandMap;

    @Autowired
    public BotCommandService(CommandConfig commandConfig) {
        this.commandMap = commandConfig.commandMap();
    }

    @Override
    public Map<String, Command> getCommandMap() {
        return commandMap;
    }

    @Override
    public String createResponse(Update update, ClientService clientService) {
        Command command = commandMap.get(update.message().text().split(" ")[0]);
        if (command == null) {
            return UNKNOWN_COMMAND;
        }
        return command.execute(update, clientService);
    }
}
