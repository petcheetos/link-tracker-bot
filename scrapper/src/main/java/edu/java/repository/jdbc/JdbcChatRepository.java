package edu.java.repository.jdbc;

import edu.java.dto.ChatDTO;
import edu.java.repository.ChatRepository;
import edu.java.utils.mappers.ChatMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(Long chatId) {
        jdbcTemplate.update("insert into chat (id) values (?)", chatId);
    }

    @Override
    public void remove(Long chatId) {
        jdbcTemplate.update("delete from chat where id = (?)", chatId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatDTO> findAll() {
        return jdbcTemplate.query("select id from chat", new ChatMapper());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exist(Long chatId) {
        Integer count = jdbcTemplate.queryForObject("select count(*) from chat where id = ?", Integer.class, chatId);
        return count != null && count > 0;
    }
}
