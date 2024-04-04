package edu.java.configuration.kafka;

import edu.java.models.LinkUpdateRequest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
@RequiredArgsConstructor
public class KafkaConfiguration {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<Integer, LinkUpdateRequest> producerFactory() {
        Map<String, Object> configs = Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers(),
            ProducerConfig.ACKS_CONFIG, kafkaProperties.acksMode(),
            ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, kafkaProperties.enableIdempotence(),
            ProducerConfig.BATCH_SIZE_CONFIG, kafkaProperties.batchSize(),
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Integer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public KafkaTemplate<Integer, LinkUpdateRequest> kafkaTemplate(
        ProducerFactory<Integer, LinkUpdateRequest> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }
}
