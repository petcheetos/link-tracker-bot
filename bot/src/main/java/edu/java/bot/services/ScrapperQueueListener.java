package edu.java.bot.services;

import edu.java.models.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScrapperQueueListener {
    private static final Logger LOGGER = LogManager.getLogger();
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;
    private final BotService botService;

    @RetryableTopic(dltTopicSuffix = "_dlq")
    @KafkaListener(topics = "${app.scrapper-topic-name}")
    public void handle(@Payload LinkUpdateRequest updateRequest, Acknowledgment ack) {
        LOGGER.info("Update message received ", updateRequest.url().toString());
        botService.execute(updateRequest);
        ack.acknowledge();
    }

    @DltHandler
    public void sendDQL(LinkUpdateRequest updateRequest) {
        try {
            LOGGER.info("Sending to DQL ", updateRequest);
            kafkaTemplate.send("messages.updates_dlq", updateRequest);
        } catch (Exception e) {
            LOGGER.error("DQL error ", e);
        }
    }
}
