package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;

public interface Command {

    String name();

    String description();

    String execute(Update update);
}
