package edu.java.repository.jdbc;

import edu.java.dto.LinkDTO;
import edu.java.models.LinkResponse;
import edu.java.repository.LinkRepository;
import edu.java.repository.mappers.LinkMapper;
import edu.java.repository.mappers.LinkResponseMapper;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public LinkResponse add(long chatId, URI link) {
        Long linkId = jdbcTemplate.queryForObject(
            "insert into link (url) values (?) returning id",
            Long.class,
            link.toString()
        );
        jdbcTemplate.update("insert into chat_links (chat_id, link_id) values (?, ?)", chatId, linkId);
        return new LinkResponse(linkId, link);
    }

    @Override
    @Transactional
    public LinkResponse remove(long chatId, URI link) {
        Long linkId = exist(link);
        LinkResponse response = null;
        if (linkId != null) {
            response =
                jdbcTemplate.queryForObject("select * from link where id = (?)", new LinkResponseMapper(), linkId);
            jdbcTemplate.update("delete from chat_links where chat_id = ? and link_id = ?", chatId, linkId);
        }
        return response;
    }

    @Override
    @Transactional
    public List<LinkResponse> findAllByChat(long chatId) {
        return jdbcTemplate.query("select link.* from link " +
                "join chat_links on link.id = chat_links.link_id where chat_links.chat_id = ?",
            new LinkResponseMapper(), chatId
        );
    }

    @Override
    @Transactional
    public void updateLink(LinkDTO linkDTO) {
        OffsetDateTime currentTime = OffsetDateTime.now();
        jdbcTemplate.update("update link set last_updated = (?), checked_at = (?) "
            + "where url = (?)", linkDTO.lastUpdated(), currentTime, linkDTO.url().toString());
    }

    @Override
    public LinkDTO getLinkDTO(long linkId) {
        return jdbcTemplate.queryForObject("select url, last_updated from link "
            + "where id = (?)", new LinkMapper(), linkId);
    }

    @Transactional
    protected Long exist(URI link) {
        List<Long> result = jdbcTemplate.query(
            "select id from link where url = ?",
            (rs, rowNum) -> rs.getLong("id"),
            link.toString()
        );

        return result.isEmpty() ? null : result.getFirst();
    }

    @Transactional
    protected Long getChatIdForLink(Long linkId) {
        if (linkId == null) {
            return null;
        }
        List<Long> chatIds = jdbcTemplate.query(
            "select chat_id from chat_links where link_id = ?",
            (rs, rowNum) -> rs.getLong("chat_id"),
            linkId
        );
        return chatIds.isEmpty() ? null : chatIds.getFirst();
    }
}
