package edu.java.scrapper.repository;

import edu.java.dto.ChatDTO;
import edu.java.repository.ChatRepository;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JdbcChatRepositoryTest extends IntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    public void testAddWithNewChatId() {
        long chatId = 10L;
        chatRepository.add(chatId);
        Long idCount = jdbcTemplate.queryForObject("select count(*) from chat where id = ?", Long.class, chatId);
        assertThat(idCount).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveWithExistedChatId() {
        long chatId = 10L;
        chatRepository.add(chatId);
        chatRepository.remove(chatId);
        Long idCount = jdbcTemplate.queryForObject("select count(*) from chat where id = ?", Long.class, chatId);
        assertThat(idCount).isEqualTo(0L);
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAll() {
        chatRepository.add(10L);
        chatRepository.add(20L);
        chatRepository.add(30L);
        List<ChatDTO> list = chatRepository.findAll();
        assertThat(list).contains(new ChatDTO(10L), new ChatDTO(20L), new ChatDTO(30L));
    }
}
