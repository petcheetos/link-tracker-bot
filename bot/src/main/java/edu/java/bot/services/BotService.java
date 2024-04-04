package edu.java.bot.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.models.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotService {
    private final TelegramBot telegramBot;

    public void execute(SendMessage sendMessage) {
        telegramBot.execute(sendMessage);
    }

    public void execute(LinkUpdateRequest linkRequest) {
        for (var chatId : linkRequest.tgChatIds()) {
            telegramBot.execute(new SendMessage(chatId, linkRequest.description() + linkRequest.url()));
        }
    }
}
