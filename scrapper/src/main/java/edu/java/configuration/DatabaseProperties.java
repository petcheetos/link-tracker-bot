package edu.java.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.datasource", ignoreUnknownFields = false)
public record DatabaseProperties(String driverClassName,
                                 String url,
                                 String username,
                                 String password) {
}
