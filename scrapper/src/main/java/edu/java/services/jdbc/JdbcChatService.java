package edu.java.services.jdbc;

import edu.java.repository.ChatRepository;
import edu.java.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcChatService implements ChatService {

    private final ChatRepository chatRepository;

    @Override
    public void registerChat(long chatId) {
        chatRepository.add(chatId);
    }

    @Override
    public void deleteChat(long chatId) {
        chatRepository.remove(chatId);
    }
}
