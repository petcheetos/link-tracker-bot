package edu.java.repository.jdbc;

import edu.java.dto.ChatLinkDTO;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.mappers.ChatLinkExtractor;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcChatLinkRepository implements ChatLinkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public List<ChatLinkDTO> findAllByTime(OffsetDateTime time) {
        return jdbcTemplate.query("select link.id, link.checked_at, chat_links.chat_id "
            + "from link join chat_links on chat_links.link_id = link.id "
            + "where checked_at < (?) limit 10", new ChatLinkExtractor(), time);
    }
}
