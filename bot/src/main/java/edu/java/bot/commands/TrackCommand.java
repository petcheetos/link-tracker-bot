package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.services.ClientService;
import edu.java.models.AddLinkRequest;
import java.net.URI;
import org.springframework.stereotype.Component;
import static edu.java.bot.configuration.MessageConstants.TRACK_DESCRIPTION;
import static edu.java.bot.configuration.MessageConstants.TRACK_EMPTY_LINK;
import static edu.java.bot.configuration.MessageConstants.TRACK_NAME;
import static edu.java.bot.configuration.MessageConstants.TRACK_SUCCEED;

@Component
public class TrackCommand implements Command {

    @Override
    public String name() {
        return TRACK_NAME;
    }

    @Override
    public String description() {
        return TRACK_DESCRIPTION;
    }

    @Override
    public String execute(Update update, ClientService clientService) {
        long chatId = update.message().chat().id();
        String text = update.message().text();
        String[] str = text.split(" ");
        if (str.length == 1) {
            return TRACK_EMPTY_LINK;
        } else {
            URI link = URI.create(str[1]);
            clientService.addLink(chatId, new AddLinkRequest(link));
            return TRACK_SUCCEED + link;
        }
    }
}
