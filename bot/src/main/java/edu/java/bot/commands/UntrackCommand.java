package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.services.ClientService;
import edu.java.models.RemoveLinkRequest;
import java.net.URI;
import org.springframework.stereotype.Component;
import static edu.java.bot.configuration.MessageConstants.UNTRACK_DESCRIPTION;
import static edu.java.bot.configuration.MessageConstants.UNTRACK_EMPTY_LINK;
import static edu.java.bot.configuration.MessageConstants.UNTRACK_NAME;
import static edu.java.bot.configuration.MessageConstants.UNTRACK_SUCCEED;

@Component
public class UntrackCommand implements Command {

    @Override
    public String name() {
        return UNTRACK_NAME;
    }

    @Override
    public String description() {
        return UNTRACK_DESCRIPTION;
    }

    @Override
    public String execute(Update update, ClientService clientService) {
        long chatId = update.message().chat().id();
        String text = update.message().text();
        String[] str = text.split(" ");
        if (str.length == 1) {
            return UNTRACK_EMPTY_LINK;
        } else {
            URI link = URI.create(str[1]);
            clientService.removeLink(chatId, new RemoveLinkRequest(link));
            return UNTRACK_SUCCEED + link;
        }
    }
}
