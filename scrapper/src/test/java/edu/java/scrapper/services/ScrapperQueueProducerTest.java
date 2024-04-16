package edu.java.scrapper.services;

import edu.java.configuration.kafka.KafkaProperties;
import edu.java.models.LinkUpdateRequest;
import edu.java.services.ScrapperQueueProducer;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScrapperQueueProducerTest {
    @Mock
    private KafkaTemplate<Integer, LinkUpdateRequest> kafkaTemplate;
    @Mock
    private KafkaProperties kafkaProperties;
    @InjectMocks
    private ScrapperQueueProducer producer;

    @Test
    public void testSendUpdate() {
        LinkUpdateRequest update = new LinkUpdateRequest(
            1L,
            URI.create("https://github.com/author/repo/"),
            "test",
            List.of(1L, 2L, 3L)
        );
        when(kafkaProperties.topic()).thenReturn("test-topic");

        producer.sendUpdate(update);

        verify(kafkaTemplate).send("test-topic", update);
        verifyNoMoreInteractions(kafkaTemplate);
    }
}
