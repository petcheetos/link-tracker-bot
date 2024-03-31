package edu.java.configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BucketConfiguration {

    @Value(value = "${bucket.count}")
    private int count;

    @Bean
    public Bucket create() {
        Bandwidth limit = Bandwidth.builder()
            .capacity(count)
            .refillIntervally(count, Duration.ofMinutes(1))
            .build();
        return Bucket.builder().addLimit(limit).build();
    }
}
