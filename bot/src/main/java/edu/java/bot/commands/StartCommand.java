package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.services.ClientService;
import org.springframework.stereotype.Component;
import static edu.java.bot.configuration.MessageConstants.START_DESCRIPTION;
import static edu.java.bot.configuration.MessageConstants.START_MSG;
import static edu.java.bot.configuration.MessageConstants.START_NAME;

@Component
public class StartCommand implements Command {

    @Override
    public String name() {
        return START_NAME;
    }

    @Override
    public String description() {
        return START_DESCRIPTION;
    }

    @Override
    public String execute(Update update, ClientService clientService) {
        clientService.registerChat(update.message().chat().id());
        return START_MSG;
    }
}
