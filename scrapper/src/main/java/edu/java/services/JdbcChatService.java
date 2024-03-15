package edu.java.services;

import edu.java.exception.RequestException;
import edu.java.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcChatService implements ChatService {

    private final ChatRepository chatRepository;

    @Override
    public void registerChat(long chatId) {
        if (chatRepository.exist(chatId)) {
            throw new RequestException("Chat already registered");
        }
        chatRepository.add(chatId);
    }

    @Override
    public void deleteChat(long chatId) {
        if (!chatRepository.exist(chatId)) {
            throw new RequestException("Chat does not exist");
        }
        chatRepository.remove(chatId);
    }
}

