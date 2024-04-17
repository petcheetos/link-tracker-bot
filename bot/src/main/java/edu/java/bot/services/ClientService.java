package edu.java.bot.services;

import edu.java.bot.clients.ScrapperClient;
import edu.java.models.AddLinkRequest;
import edu.java.models.LinkResponse;
import edu.java.models.ListLinkResponse;
import edu.java.models.RemoveLinkRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ScrapperClient scrapperClient;

    public void registerChat(Long chatId) {
        scrapperClient.registerChat(chatId);
    }

    public ListLinkResponse getLinks(Long chatId) {
        return scrapperClient.getLinks(chatId);
    }

    public LinkResponse addLink(Long chatId, AddLinkRequest addLinkRequest) {
        return scrapperClient.addLink(chatId, addLinkRequest);
    }

    public LinkResponse removeLink(Long chatId, RemoveLinkRequest removeLinkRequest) {
        return scrapperClient.deleteLink(chatId, removeLinkRequest);
    }
}
