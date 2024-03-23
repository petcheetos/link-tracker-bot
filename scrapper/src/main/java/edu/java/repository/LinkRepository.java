package edu.java.repository;

import edu.java.dto.LinkDTO;
import edu.java.models.LinkResponse;
import java.net.URI;
import java.util.List;

public interface LinkRepository {

    LinkResponse add(long chatId, URI link);

    LinkResponse remove(long chatId, URI link);

    List<LinkResponse> findAllByChat(long chatId);

    void updateLink(LinkDTO linkDTO);

    List<LinkDTO> getLinksToUpdate();

    LinkDTO findByUri(URI uri);

    List<Long> getChatIdsForLink(Long linkId);

    void checkLink(LinkDTO linkDTO);
}
