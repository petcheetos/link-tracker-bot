package edu.java.bot.controllers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.models.LinkUpdateRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/updates")
public class BotController {
    private final TelegramBot telegramBot;

    @PostMapping
    public void processUpdate(@RequestBody LinkUpdateRequest linkRequest) {
        log.info(linkRequest);
        List<Long> tgChatIds = linkRequest.tgChatIds();
        for (var chatID : tgChatIds) {
            telegramBot.execute(new SendMessage(chatID, linkRequest.description() + linkRequest.url()));
        }
    }
}
