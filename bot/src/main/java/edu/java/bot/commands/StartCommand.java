package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {

    @Override
    public String name() {
        return "/start";
    }

    @Override
    public String description() {
        return "Register a user";
    }

    @Override
    public String execute(Update update) {
        //register user
        return "Hi! I am a link tracking bot! Type /help to see the list of available commands!";
    }
}
