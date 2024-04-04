package edu.java.bot.configuration.kafka;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;

@Configuration
@RequiredArgsConstructor
public class KafkaDLQConfiguration {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<Integer, byte[]> producerFactory() {
        Map<String, Object> configs = Map.of(
            ProducerConfig.ACKS_CONFIG, kafkaProperties.dlq().acksMode(),
            ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, kafkaProperties.dlq().deliveryTimeout(),
            ProducerConfig.BATCH_SIZE_CONFIG, kafkaProperties.dlq().batchSize(),
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class
        );
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public KafkaTemplate<Integer, byte[]> kafkaTemplate(ProducerFactory<Integer, byte[]> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(KafkaTemplate<Integer, byte[]> template) {
        return new DeadLetterPublishingRecoverer(
            template,
            ((consumerRecord, e) -> new TopicPartition(kafkaProperties.dlq().topic(), consumerRecord.partition()))
        );
    }

    @Bean
    public CommonErrorHandler commonErrorHandler(DeadLetterPublishingRecoverer recoverer) {
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer);
        errorHandler.addNotRetryableExceptions(ValidateException.class);
        return errorHandler;
    }
}
