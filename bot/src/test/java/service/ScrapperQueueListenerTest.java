package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.configuration.kafka.KafkaProperties;
import edu.java.bot.services.BotService;
import edu.java.bot.services.ScrapperQueueListener;
import edu.java.models.LinkUpdateRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScrapperQueueListenerTest {
    @Mock private KafkaTemplate<Integer, byte[]> kafkaTemplate;
    @Mock private KafkaProperties kafkaProperties;
    @Mock private ObjectMapper objectMapper;
    @Mock private BotService botService;
    @Mock private Validator validator;
    @Mock private KafkaProperties.DLQProperties dlqProperties;
    @Mock private Acknowledgment acknowledgment;
    @InjectMocks private ScrapperQueueListener listener;

    @Test
    public void testListenWithValidUpdate() {
        LinkUpdateRequest update = new LinkUpdateRequest(
            1L,
            URI.create("https://github.com/author/repo/"),
            "test",
            List.of(1L, 2L, 3L)
        );
        Set<ConstraintViolation<LinkUpdateRequest>> noViolations = new HashSet<>();
        when(validator.validate(update)).thenReturn(noViolations);
        listener.listen(update, acknowledgment);
        verify(botService).execute(update);
        verify(acknowledgment).acknowledge();
    }

    @Test
    public void testListenWithInvalidUpdate() {
        LinkUpdateRequest update = new LinkUpdateRequest(
            1L,
            URI.create("https://github.com/author/repo/"),
            "test",
            List.of(1L, 2L, 3L)
        );
        when(kafkaProperties.dlq()).thenReturn(dlqProperties);
        when(dlqProperties.topic()).thenReturn("topic");
        Set<ConstraintViolation<LinkUpdateRequest>> violations = new HashSet<>();
        violations.add(mock(ConstraintViolation.class));
        when(validator.validate(update)).thenReturn(violations);
        listener.listen(update, acknowledgment);
        verify(botService, never()).execute(update);
        verify(kafkaTemplate).send(anyString(), any());
        verify(acknowledgment).acknowledge();
    }
}
