package edu.java.services;

import edu.java.models.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperQueueProducer {
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;
    private static final Logger LOGGER = LogManager.getLogger();

    public void sendUpdate(LinkUpdateRequest update) {
        try {
            kafkaTemplate.send("messages.updates", update);
        } catch (Exception e) {
            LOGGER.error("Kafka producer sent update ", e);
        }
    }
}
