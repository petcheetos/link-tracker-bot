package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.commands.Command;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {

    @Bean
    Map<String, Command> commandMap() {
        return CommandConfig.createCommandMap();
    }

    @Bean
    TelegramBot telegramBot(ApplicationConfig applicationConfig) {
        TelegramBot tgBot = new TelegramBot(applicationConfig.telegramToken());
        tgBot.execute(menu(commandMap()));
        return tgBot;
    }

    private SetMyCommands menu(Map<String, Command> commandMap) {
        Command help = commandMap.get("/help");
        Command start = commandMap.get("/start");
        Command list = commandMap.get("/list");
        Command track = commandMap.get("/track");
        Command untrack = commandMap.get("/untrack");
        return new SetMyCommands(
            new BotCommand(help.name(), help.description()),
            new BotCommand(start.name(), start.description()),
            new BotCommand(list.name(), list.description()),
            new BotCommand(track.name(), track.description()),
            new BotCommand(untrack.name(), untrack.description())
        );
    }
}
