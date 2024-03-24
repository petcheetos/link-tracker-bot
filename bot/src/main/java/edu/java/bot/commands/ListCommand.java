package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.services.ClientService;
import edu.java.models.LinkResponse;
import edu.java.models.ListLinkResponse;
import org.springframework.stereotype.Component;
import static edu.java.bot.configuration.MessageConstants.LIST_DESCRIPTION;
import static edu.java.bot.configuration.MessageConstants.LIST_EMPTY_MSG;
import static edu.java.bot.configuration.MessageConstants.LIST_NAME;
import static edu.java.bot.configuration.MessageConstants.LIST_TITLE;

@Component
public class ListCommand implements Command {

    @Override
    public String name() {
        return LIST_NAME;
    }

    @Override
    public String description() {
        return LIST_DESCRIPTION;
    }

    @Override
    public String execute(Update update, ClientService clientService) {
        long chatId = update.message().chat().id();
        ListLinkResponse list = clientService.getLinks(chatId);
        if (list.size() == 0) {
            return LIST_EMPTY_MSG;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LIST_TITLE).append("\n");
        for (LinkResponse link : list.links()) {
            stringBuilder.append(link.url().toString()).append("\n");
        }
        return stringBuilder.toString();
    }
}
