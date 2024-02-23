package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand implements Command {
    @Override
    public String name() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Remove a site from the tracking list";
    }

    @Override
    public String execute(Update update) {
        //change user status
        return "What site do you want to stop receiving updates from? Please, send a link!";
    }
}
