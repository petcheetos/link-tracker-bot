package edu.java.bot.configuration.kafka;

import edu.java.models.LinkUpdateRequest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
@RequiredArgsConstructor
public class KafkaConfiguration {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<Integer, LinkUpdateRequest> consumerFactory() {
        Map<String, Object> configs = Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers(),
            ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.groupId(),
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.autoOffsetReset(),
            ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafkaProperties.maxPollIntervalMs(),
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaProperties.enableAutoCommit(),
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
            ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName(),
            JsonDeserializer.VALUE_DEFAULT_TYPE, LinkUpdateRequest.class
        );
        return new DefaultKafkaConsumerFactory<>(configs);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Integer, LinkUpdateRequest> containerFactory(
        ConsumerFactory<Integer, LinkUpdateRequest> consumerFactory,
        CommonErrorHandler commonErrorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<Integer, LinkUpdateRequest> container =
            new ConcurrentKafkaListenerContainerFactory<>();
        container.setConcurrency(kafkaProperties.concurrency());
        container.setConsumerFactory(consumerFactory);
        container.setCommonErrorHandler(commonErrorHandler);
        return container;
    }
}
