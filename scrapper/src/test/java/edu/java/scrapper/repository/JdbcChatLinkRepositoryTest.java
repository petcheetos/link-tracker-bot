package edu.java.scrapper.repository;

import edu.java.dto.ChatLinkDTO;
import edu.java.dto.LinkDTO;
import edu.java.models.LinkResponse;
import edu.java.repository.LinkRepository;
import edu.java.repository.jdbc.JdbcChatLinkRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JdbcChatLinkRepositoryTest extends IntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcChatLinkRepository jdbcChatLinkRepository;

    @Test
    @Transactional
    @Rollback
    public void testFindAllByTime() {
        LinkRepository linkRepository = new JdbcLinkRepository(jdbcTemplate);
        long chatId = 1L;
        long yaChatId = 2L;
        String url1 = "github.com";
        String url2 = "stackoverflow.com";
        LinkResponse linkResponse = linkRepository.add(chatId, URI.create(url1));
        LinkResponse linkResponse2 = linkRepository.add(yaChatId, URI.create(url2));
        List<ChatLinkDTO> expected = List.of(
            new ChatLinkDTO(linkResponse2.id(), Set.of(yaChatId))
        );
        linkRepository.updateLink(new LinkDTO(linkResponse.id(), linkResponse.url(), OffsetDateTime.parse("1980-04-09T08:20:45+07:00")));
        linkRepository.updateLink(new LinkDTO(linkResponse2.id(), linkResponse2.url(), OffsetDateTime.now()));
        List<ChatLinkDTO> chatLinkDTOS = jdbcChatLinkRepository.findAllByTime(OffsetDateTime.parse("1985-04-09T08:20:45+07:00"));
        assertThat(chatLinkDTOS).isEqualTo(expected);
    }
}
