package edu.java.services.jpa;

import edu.java.dto.entity.Chat;
import edu.java.exception.RequestException;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.services.ChatService;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaChatService implements ChatService {

    private final JpaChatRepository chatRepository;

    @Override
    @Transactional
    public void registerChat(long chatId) {
        if (chatRepository.existsById(chatId)) {
            throw new RequestException("Chat already registered");
        }
        Chat chat = new Chat(chatId, new HashSet<>());
        chatRepository.save(chat);
    }

    @Override
    @Transactional
    public void deleteChat(long chatId) {
        chatRepository.findById(chatId).ifPresentOrElse(chat -> {
            chatRepository.delete(chat);
            chatRepository.flush();
        }, () -> {
            throw new RequestException("Chat is not registered");
        });
    }
}
