package edu.java.bot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.configuration.kafka.KafkaProperties;
import edu.java.models.LinkUpdateRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScrapperQueueListener {
    private static final Logger LOGGER = LogManager.getLogger();
    private final KafkaTemplate<Integer, byte[]> kafkaTemplate;
    private final KafkaProperties kafkaProperties;
    private final ObjectMapper objectMapper;
    private final BotService botService;
    private final Validator validator;

    @KafkaListener(topics = "${app.scrapper-topic-name}", containerFactory = "containerFactory")
    public void listen(@Payload LinkUpdateRequest updateRequest, Acknowledgment acknowledgment) {
        try {
            if (checkUpdateIsValid(updateRequest)) {
                botService.execute(updateRequest);
                LOGGER.info("Listener processed update: ", updateRequest);
            } else {
                LOGGER.info("Listener update is not valid ", updateRequest);
                sendDQL(updateRequest);
            }
        } catch (Exception e) {
            LOGGER.error(e);
            sendDQL(updateRequest);
        } finally {
            acknowledgment.acknowledge();
        }
    }

    private boolean checkUpdateIsValid(LinkUpdateRequest updateRequest) {
        Set<ConstraintViolation<LinkUpdateRequest>> violations = validator.validate(updateRequest);
        return violations.isEmpty();
    }

    private void sendDQL(LinkUpdateRequest updateRequest) {
        try {
            LOGGER.info("Sending to DQL ", updateRequest);
            kafkaTemplate.send(kafkaProperties.dlq().topic(), objectMapper.writeValueAsBytes(updateRequest));
        } catch (JsonProcessingException e) {
            LOGGER.error("DQL error ", e);
        }
    }
}
