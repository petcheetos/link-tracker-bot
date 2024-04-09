package edu.java.repository;

import edu.java.dto.ChatLinkDTO;
import java.time.OffsetDateTime;
import java.util.List;

public interface ChatLinkRepository {
    List<ChatLinkDTO> findAllByTime(OffsetDateTime time);
}
