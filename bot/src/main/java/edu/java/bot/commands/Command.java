package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.services.ClientService;

public interface Command {

    String name();

    String description();

    String execute(Update update, ClientService clientService);
}
