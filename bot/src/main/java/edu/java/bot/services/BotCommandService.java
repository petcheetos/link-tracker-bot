package edu.java.bot.services;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.Command;
import edu.java.bot.configuration.CommandConfig;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public String createResponse(Update update) {
        String msgText = update.message().text();
        Command command = commandMap.get(msgText);
        if (command == null) {
            return processNonCommandMsg(update);
        }
        return command.execute(update);
    }

    private String processNonCommandMsg(Update update) {
        String text = update.message().text();
        Pattern pattern = Pattern.compile("^/");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return "I don't know this command! Type /help to find out about my commands";
        }
        return "...";
    }
}
