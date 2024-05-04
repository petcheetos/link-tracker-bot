package edu.java.repository.jdbc;

import edu.java.dto.entity.Link;
import edu.java.models.LinkResponse;
import edu.java.repository.LinkRepository;
import edu.java.utils.mappers.LinkMapper;
import edu.java.utils.mappers.LinkResponseMapper;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
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
        Link linkDTO;
        try {
            linkDTO = findByUri(link);
        } catch (DataAccessException e) {
            jdbcTemplate.update(
                "insert into link (url, last_updated, checked_at) values (?, ?, ?)",
                link.toString(),
                OffsetDateTime.now(),
                OffsetDateTime.now()
            );
            linkDTO = findByUri(link);
        }
        jdbcTemplate.update("insert into chat_links (chat_id, link_id) values (?, ?)", chatId, linkDTO.getId());
        return new LinkResponse(linkDTO.getId(), URI.create(linkDTO.getUrl()));
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
    @Transactional(readOnly = true)
    public List<LinkResponse> findAllByChat(long chatId) {
        return jdbcTemplate.query("select link.* from link "
                + "join chat_links on link.id = chat_links.link_id where chat_links.chat_id = ?",
            new LinkResponseMapper(), chatId
        );
    }

    @Override
    @Transactional
    public void updateLastUpdatedAt(String link, OffsetDateTime time) {
        OffsetDateTime currentTime = OffsetDateTime.now();
        jdbcTemplate.update("update link set last_updated = (?), checked_at = (?) where url = (?)",
            time, currentTime, link
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Link> getLinksToUpdate(OffsetDateTime time) {
        return jdbcTemplate.query(
            "select * from link where checked_at < timezone('utc', now() - ?)",
            new LinkMapper(), time
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Link findByUri(URI uri) throws DataAccessException {
        return jdbcTemplate.queryForObject("select * from link where url = (?)",
            new LinkMapper(), uri.toString()
        );
    }

    @Transactional(readOnly = true)
    protected Long exist(URI link) {
        List<Long> result = jdbcTemplate.query(
            "select id from link where url = ?",
            (rs, rowNum) -> rs.getLong("id"),
            link.toString()
        );

        return result.isEmpty() ? null : result.getFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> findChatIdsByLink(Link link) {
        String link1 = link.getUrl();
        List<Long> chatIds = jdbcTemplate.query(
            "select chat_id from chat_links inner join link on chat_links.link_id = link.id where url = ?",
            new Object[] {link1},
            (rs, rowNum) -> rs.getLong("chat_id")
        );
        return chatIds.isEmpty() ? null : chatIds;
    }

    @Override
    @Transactional
    public void updateCheckedAt(Link link) {
        OffsetDateTime currentTime = OffsetDateTime.now();
        jdbcTemplate.update("update link set checked_at = (?) "
            + "where url = (?)", currentTime, link.getUrl());
    }
}
