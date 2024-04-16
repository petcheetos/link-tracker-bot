package edu.java.services.jpa;

import edu.java.dto.entity.Chat;
import edu.java.dto.entity.Link;
import edu.java.exception.RequestException;
import edu.java.models.LinkResponse;
import edu.java.models.ListLinkResponse;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.repository.jpa.JpaLinkRepositoryInterface;
import edu.java.services.LinkService;
import edu.java.utils.LinkProcessor;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final LinkProcessor linkValidator;
    private final JpaLinkRepositoryInterface linkRepository;
    private final JpaChatRepository chatRepository;

    @Override
    @Transactional
    public List<LinkResponse> getLinks(long chatId) {
        checkChatIfNotExist(chatId);
        return chatRepository.findById(chatId).map(
            chat -> new ListLinkResponse(
                chat.getTrackedLinks().stream().map(
                    link -> new LinkResponse(link.getId(), URI.create(link.getUrl()))
                ).toList(), 1
            )
        ).get().links();
    }

    @Override
    @Transactional
    public LinkResponse addLink(long chatId, URI link) {
        checkChatIfNotExist(chatId);
        if (!linkValidator.isValid(link)) {
            throw new RequestException("Sorry, so far I can only track GitHub and StackOverflow \uD83D\uDE1F");
        }
        Chat chat = chatRepository.findById(chatId).get();
        var optionalLink = linkRepository.findByUrl(link.toString());
        if (optionalLink.isPresent()) {
            Link linkEntity = optionalLink.get();
            chat.addLink(linkEntity);
            return new LinkResponse(linkEntity.getId(), link);
        }
        Link linkEntity = new Link(link.toString());
        linkRepository.save(linkEntity);
        chat.addLink(linkEntity);
        return new LinkResponse(linkEntity.getId(), URI.create(linkEntity.getUrl()));
    }

    @Override
    @Transactional
    public LinkResponse deleteLink(long chatId, URI link) {
        checkChatIfNotExist(chatId);
        Chat chat = chatRepository.findById(chatId).get();
        var linkEntityOptional = linkRepository.findByUrl(link.toString());
        if (linkEntityOptional.isPresent()) {
            Link linkEntity = linkEntityOptional.get();
            chat.removeLink(linkEntity);
            return new LinkResponse(linkEntity.getId(), link);
        }
        return null;
    }

    void checkChatIfNotExist(long chatId) {
        if (!chatRepository.existsById(chatId)) {
            throw new RequestException("Chat does not exist");
        }
    }
}
