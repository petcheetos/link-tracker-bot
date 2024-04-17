package edu.java.bot.services;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.Command;
import java.util.Map;

public interface CommandService {

    String createResponse(Update update, ClientService clientService);

    Map<String, Command> getCommandMap();
}
