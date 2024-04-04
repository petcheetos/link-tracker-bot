package edu.java.services;

import edu.java.clients.BotClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.models.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Sender {
    private final ApplicationConfig applicationConfig;
    private final ScrapperQueueProducer scrapperQueueProducer;
    private final BotClient botClient;

    public void sendUpdate(LinkUpdateRequest updateRequest) {
        if (applicationConfig.useQueue()) {
            scrapperQueueProducer.sendUpdate(updateRequest);
        } else {
            botClient.sendUpdate(updateRequest);
        }
    }
}
