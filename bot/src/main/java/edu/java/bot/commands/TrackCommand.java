package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command {

    @Override
    public String name() {
        return "/track";
    }

    @Override
    public String description() {
        return "Add link for tracking";
    }

    @Override
    public String execute(Update update) {
        //change user status
        return "What site do you want to track updates to? Please, send a link!";
    }
}
