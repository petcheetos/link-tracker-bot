package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.services.ClientService;
import java.util.List;
import org.springframework.stereotype.Component;
import static edu.java.bot.configuration.MessageConstants.HELP_DESCRIPTION;
import static edu.java.bot.configuration.MessageConstants.HELP_NAME;
import static edu.java.bot.configuration.MessageConstants.HELP_TITLE;

@Component
public class HelpCommand implements Command {
    List<Command> commandList;

    public HelpCommand(List<Command> commandList) {
        this.commandList = commandList;
    }

    @Override
    public String name() {
        return HELP_NAME;
    }

    @Override
    public String description() {
        return HELP_DESCRIPTION;
    }

    @Override
    public String execute(Update update, ClientService clientService) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HELP_TITLE);
        for (Command cmd : commandList) {
            stringBuilder.append("\t■ ").append(cmd.name()).append(" - ").append(cmd.description()).append("\n");
        }
        return stringBuilder.toString();
    }
}
