package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exceptions.ApiErrorException;
import edu.java.bot.services.ClientService;
import edu.java.bot.services.CommandService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageHandler implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final CommandService commandService;
    private final ClientService clientService;

    @Autowired
    public MessageHandler(TelegramBot telegramBot, CommandService commandService, ClientService clientService) {
        this.telegramBot = telegramBot;
        this.commandService = commandService;
        this.clientService = clientService;
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            if (update.message() != null && update.message().text() != null) {
                try {
                    String answer = commandService.createResponse(update, clientService);
                    SendMessage request = new SendMessage(update.message().chat().id(), answer);
                    telegramBot.execute(request);
                } catch (ApiErrorException e) {
                    telegramBot.execute(new SendMessage(update.message().chat().id(), e.getMessage()));
                }
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
