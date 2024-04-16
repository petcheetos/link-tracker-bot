package edu.java.repository;

import edu.java.dto.entity.Link;
import edu.java.models.LinkResponse;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkRepository {
    LinkResponse add(long chatId, URI link);

    LinkResponse remove(long chatId, URI link);

    List<LinkResponse> findAllByChat(long chatId);

    void updateLastUpdatedAt(String link, OffsetDateTime time);

    List<Link> getLinksToUpdate(OffsetDateTime time);

    Link findByUri(URI uri);

    List<Long> findChatIdsByLink(Link link);

    void updateCheckedAt(Link link);
}
