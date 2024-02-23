package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.Command;
import java.util.Map;

public interface CommandService {

    String createResponse(Update update);

    Map<String, Command> getCommandMap();
}
