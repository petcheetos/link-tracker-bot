package edu.java.services;

import edu.java.exception.RequestException;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {

    void registerChat(long chatId);

    void deleteChat(long chatId);
}
