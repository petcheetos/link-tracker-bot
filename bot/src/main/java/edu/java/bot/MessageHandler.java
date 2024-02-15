package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MessageHandler implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final MessageService messageService;

    public MessageHandler(TelegramBot telegramBot, MessageService messageService) {
        this.telegramBot = telegramBot;
        this.messageService = messageService;
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            if (update.message() != null && update.message().text() != null) {
                String answer = messageService.createResponse(update);
                SendMessage request = new SendMessage(update.message().chat().id(), answer);
                telegramBot.execute(request);
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
