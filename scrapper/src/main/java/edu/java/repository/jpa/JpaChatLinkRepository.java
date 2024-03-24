package edu.java.repository.jpa;

import edu.java.dto.entity.ChatLink;
import edu.java.dto.entity.ChatLinkId;
import edu.java.dto.entity.Link;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaChatLinkRepository extends JpaRepository<ChatLink, ChatLinkId> {

    Optional<ChatLink> findChatLinkByChatIdAndLinkUrl(Long chatId, String url);

    @Query("select link from ChatLink where chat.id = :chatId")
    List<Link> findAllLinksByChatId(Long chatId);

    boolean existsByLinkId(Long linkId);
}
