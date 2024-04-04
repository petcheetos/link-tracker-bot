package edu.java.services;

import edu.java.configuration.kafka.KafkaProperties;
import edu.java.models.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperQueueProducer {
    private final KafkaTemplate<Integer, LinkUpdateRequest> kafkaTemplate;
    private final KafkaProperties kafkaProperties;
    private static final Logger LOGGER = LogManager.getLogger();

    public void sendUpdate(LinkUpdateRequest update) {
        try {
            kafkaTemplate.send(kafkaProperties.topic(), update);
        } catch (Exception e) {
            LOGGER.error("Kafka producer sent update ", e);
        }
    }
}
