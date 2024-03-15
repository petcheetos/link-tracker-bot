package edu.java.repository;

import edu.java.models.LinkResponse;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final LinkMapper linkMapper = new LinkMapper();
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void add(long chatId, URI link) {
        jdbcTemplate.update("insert into link (url, last_updated) values (?, CURRENT_TIMESTAMP)", link.toString());
    }

    @Override
    @Transactional
    public void remove(long chatId, URI link) {
        Long linkId = jdbcTemplate.queryForObject("select id from link where url = ?", Long.class, link.toString());
        if (linkId != null) {
            jdbcTemplate.update("delete from chat_links where chat_id = ? and link_id = ?", chatId, linkId);
        }
    }

    @Override
    @Transactional
    public List<LinkResponse> findAll(long chatId) {
        return jdbcTemplate.query("select link.id, link.url from link " +
                "join chat_links on link.id = chat_links.link_id where chat_links.chat_id = ?",
            new LinkResponseMapper(), chatId
        );
//        return jdbcTemplate.query("select link.* from link " +
//            "join chat_links on link.id = chat_links.link_id where chat_links.chat_id = ?", linkMapper, chatId);
    }

}
