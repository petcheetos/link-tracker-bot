package edu.java.repository.jpa;

import edu.java.dto.entity.Link;
import edu.java.models.LinkResponse;
import edu.java.repository.LinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JpaLinkRepository implements LinkRepository {

    private final JpaLinkRepositoryInterface jpaLinkRepository;
    private final JpaChatRepository chatRepository;

    @Override
    @Transactional
    public LinkResponse add(long chatId, URI link) {
        jpaLinkRepository.save(new Link(
            chatId,
            link.toString(),
            OffsetDateTime.now(),
            OffsetDateTime.now(),
            Set.of(chatRepository.findById(chatId).get())
        ));
        return new LinkResponse(chatId, link);
    }

    @Override
    @Transactional
    public LinkResponse remove(long chatId, URI link) {
        jpaLinkRepository.delete(new Link(
            chatId,
            link.toString(),
            OffsetDateTime.now(),
            OffsetDateTime.now(),
            Set.of(chatRepository.findById(chatId).get())
        ));
        return new LinkResponse(chatId, link);
    }

    @Override
    public List<LinkResponse> findAllByChat(long chatId) {
        return jpaLinkRepository.getLinksByChatId(chatId).stream()
            .map(link -> new LinkResponse(link.getId(), URI.create(link.getUrl())))
            .collect(Collectors.toList());
    }

    @Override
    public void updateLastUpdatedAt(String link, OffsetDateTime time) {
        jpaLinkRepository.updateLastUpdatedAt(link, time);
    }

    @Override
    public List<Link> getLinksToUpdate(OffsetDateTime time) {
        return jpaLinkRepository.getLinksToUpdate(time);
    }

    @Override
    public Link findByUri(URI uri) {
        return jpaLinkRepository.findByUrl(uri.toString()).get();
    }

    @Override
    public List<Long> findChatIdsByLink(Link link) {
        return jpaLinkRepository.findChatIdsByLink(link);
    }

    @Override
    public void updateCheckedAt(Link link) {
        jpaLinkRepository.updateCheckedAt(link.getUrl(), OffsetDateTime.now());
    }
}
