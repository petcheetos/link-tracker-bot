package edu.java.configuration.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka", ignoreUnknownFields = false)
public record KafkaProperties(
    @Value("topic") String topic,
    @Value("bootstrap-servers") String bootstrapServers,
    @Value("replication-factor") short replicationFactor,
    @Value("partitions") int partitions,
    @Value("acks-mode") String acksMode,
    @Value("delivery-timeout") Integer deliveryTimeout,
    @Value("batch-size") Integer batchSize,
    @Value("enable-idempotence") Boolean enableIdempotence
) {
}
