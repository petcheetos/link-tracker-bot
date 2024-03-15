package edu.java.services;

import edu.java.exception.RequestException;
import edu.java.models.LinkResponse;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;

    @Override
    public List<LinkResponse> getLinks(long chatId) {
        checkChatDoesNotExist(chatId);
        return linkRepository.findAll(chatId);
    }

    @Override
    public LinkResponse addLink(long chatId, URI link) {

    }

    @Override
    public LinkResponse deleteLink(long chatId, URI link) {

    }

    private void checkChatDoesNotExist(long chatId) {
        if (!chatRepository.exist(chatId)) {
            throw new RequestException("Chat does not exist");
        }
    }
}

//
//    public List<LinkResponse> getLinks(long chatId) {
//        checkChatDoesNotExist(chatId);
//        return chatLinks.get(chatId);
//    }
//
//    public LinkResponse addLink(long chatId, URI link) {
//        checkChatDoesNotExist(chatId);
//        LinkResponse linkResponse = new LinkResponse(chatId, link);
//        List<LinkResponse> linkResponseList = chatLinks.get(chatId);
//        if (!linkResponseList.contains(linkResponse)) {
//            linkResponseList.add(linkResponse);
//        }
//        return linkResponse;
//    }
//
//    public LinkResponse deleteLink(long chatId, URI link) {
//        checkChatDoesNotExist(chatId);
//        List<LinkResponse> linkResponseList = chatLinks.get(chatId);
//        for (LinkResponse linkResponse : linkResponseList) {
//            if (linkResponse.url().getPath().equals(link.getPath())) {
//                linkResponseList.remove(linkResponse);
//                return linkResponse;
//            }
//        }
//        throw new LinkNotFoundException("No link");
//    }
//
//    private void checkChatDoesNotExist(long chatId) {
//        if (!chatLinks.containsKey(chatId)) {
//            throw new RequestException("Chat does not exist");
//        }
//    }
