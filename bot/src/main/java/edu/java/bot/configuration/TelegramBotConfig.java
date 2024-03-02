package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.commands.Command;
import edu.java.bot.services.CommandService;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {

    @Bean
    public TelegramBot telegramBot(ApplicationConfig applicationConfig, CommandService commandService) {
        TelegramBot tgBot = new TelegramBot(applicationConfig.telegramToken());
        tgBot.execute(menu(commandService.getCommandMap()));
        return tgBot;
    }

    private SetMyCommands menu(Map<String, Command> commandMap) {
        BotCommand[] botCommands = commandMap.entrySet().stream()
            .map(entry -> new BotCommand(entry.getKey(), entry.getValue().description()))
            .toArray(BotCommand[]::new);
        return new SetMyCommands(botCommands);
    }
}
