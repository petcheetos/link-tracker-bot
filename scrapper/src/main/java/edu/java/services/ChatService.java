package edu.java.services;

import edu.java.exception.LinkNotFoundException;
import edu.java.exception.RequestException;
import edu.java.models.LinkResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final Map<Long, List<LinkResponse>> chatLinks = new HashMap<>();

    public void registerChat(long chatId) {
        if (chatLinks.containsKey(chatId)) {
            throw new RequestException("Chat already registered");
        }
        chatLinks.put(chatId, new ArrayList<>());
    }

    public void deleteChat(long chatId) {
        checkChatDoesNotExist(chatId);
        chatLinks.remove(chatId);
    }

    public List<LinkResponse> getLinks(long chatId) {
        checkChatDoesNotExist(chatId);
        return chatLinks.get(chatId);
    }

    public LinkResponse addLink(long chatId, URI link) {
        checkChatDoesNotExist(chatId);
        LinkResponse linkResponse = new LinkResponse(chatId, link);
        List<LinkResponse> linkResponseList = chatLinks.get(chatId);
        if (!linkResponseList.contains(linkResponse)) {
            linkResponseList.add(linkResponse);
        }
        return linkResponse;
    }

    public LinkResponse deleteLink(long chatId, URI link) {
        checkChatDoesNotExist(chatId);
        List<LinkResponse> linkResponseList = chatLinks.get(chatId);
        for (LinkResponse linkResponse : linkResponseList) {
            if (linkResponse.url().getPath().equals(link.getPath())) {
                linkResponseList.remove(linkResponse);
                return linkResponse;
            }
        }
        throw new LinkNotFoundException("No link");
    }

    private void checkChatDoesNotExist(long chatId) {
        if (!chatLinks.containsKey(chatId)) {
            throw new RequestException("Chat does not exist");
        }
    }
}
