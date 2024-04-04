package edu.java.bot.configuration.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka", ignoreUnknownFields = false)
public record KafkaProperties(
    @Value("group-id") String groupId,
    @Value("bootstrap-servers") String bootstrapServers,
    @Value("auto-offset-reset") String autoOffsetReset,
    @Value("max-poll-interval-ms") Integer maxPollIntervalMs,
    @Value("enable-auto-commit") Boolean enableAutoCommit,
    @Value("concurrency") Integer concurrency,
    @Value("dlq") DLQProperties dlq
) {
    public record DLQProperties(
        @Value("dlq.topic") String topic,
        @Value("dlq.replication-factor") Integer replicationFactor,
        @Value("dlq.partitions") Integer partitions,
        @Value("dlq.acks-mode") String acksMode,
        @Value("dlq.delivery-timeout") Integer deliveryTimeout,
        @Value("dlq.batch-size") Integer batchSize
    ) {
    }
}
