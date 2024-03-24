package edu.java.services.jpa;

import edu.java.dto.entity.Chat;
import edu.java.dto.entity.ChatLink;
import edu.java.dto.entity.ChatLinkId;
import edu.java.dto.entity.Link;
import edu.java.exception.RequestException;
import edu.java.models.LinkResponse;
import edu.java.repository.jpa.JpaChatLinkRepository;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.services.LinkService;
import edu.java.utils.LinkProcessor;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final LinkProcessor linkValidator;
    private final JpaLinkRepository linkRepository;
    private final JpaChatLinkRepository chatLinkRepository;
    private final JpaChatRepository chatRepository;

    @Override
    @Transactional
    public List<LinkResponse> getLinks(long chatId) {
        checkChatIfNotExist(chatId);
        return chatLinkRepository.findAllLinksByChatId(chatId)
            .stream()
            .map(link -> new LinkResponse(link.getId(), URI.create(link.getUrl())))
            .toList();
    }

    @Override
    @Transactional
    public LinkResponse addLink(long chatId, URI link) {
        checkChatIfNotExist(chatId);
        if (!linkValidator.isValid(link)) {
            throw new RequestException("Incorrect link");
        }
        Chat chat = chatRepository.getReferenceById(chatId);
        Optional<Link> linkOptional = linkRepository.findByUrl(link.toString());
        Link newLink = new Link();
        if (linkOptional.isEmpty()) {
            newLink.setUrl(link.toString());
            newLink.setLastUpdated(OffsetDateTime.now());
            newLink.setCheckedAt(OffsetDateTime.now());
            newLink.setChatLinks(new ArrayList<>());
            linkRepository.save(newLink);
        }
        ChatLink chatLink = new ChatLink();
        ChatLinkId chatLinkId = new ChatLinkId();
        chatLinkId.setChatId(chatId);
        chatLinkId.setLinkId(newLink.getId());
        chatLink.setChat(chat);
        chatLink.setLink(newLink);
        chatLinkRepository.save(chatLink);
        return new LinkResponse(newLink.getId(), URI.create(newLink.getUrl()));
    }

    @Override
    @Transactional
    public LinkResponse deleteLink(long chatId, URI link) {
        checkChatIfNotExist(chatId);
        Optional<ChatLink> chatLinkOptional =
            chatLinkRepository.findChatLinkByChatIdAndLinkUrl(chatId, link.toString());
        if (chatLinkOptional.isPresent()) {
            ChatLink chatLink = chatLinkOptional.get();
            chatLinkRepository.delete(chatLink);
            Link link1 = chatLink.getLink();
            if (!chatLinkRepository.existsByLinkId(link1.getId())) {
                linkRepository.delete(link1);
            }
            return new LinkResponse(link1.getId(), URI.create(link1.getUrl()));
        }
        return null;
    }

    @Transactional
    void checkChatIfNotExist(long chatId) {
        if (!chatRepository.existsById(chatId)) {
            throw new RequestException("Chat does not exist");
        }
    }
}
