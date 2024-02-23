package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {
    //private final userDataBase?

    @Override
    public String name() {
        return "/list";
    }

    @Override
    public String description() {
        return "Send a list of tracking sites";
    }

    @Override
    public String execute(Update update) {
        //check list
        //if (list.isEmpty()) return "List is empty!"  else return list
        return "List of tracking sites:";
    }
}
