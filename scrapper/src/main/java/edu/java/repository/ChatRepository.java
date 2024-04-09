package edu.java.repository;

import edu.java.dto.ChatDTO;
import java.util.List;

public interface ChatRepository {

    void add(Long chatId);

    void remove(Long chatId);

    List<ChatDTO> findAll();

    boolean exist(Long chatId);
}
