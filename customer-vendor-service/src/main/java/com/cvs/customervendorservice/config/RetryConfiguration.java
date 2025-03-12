package com.cvs.customervendorservice.config;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RetryConfiguration {

    @Bean
    public Retry addressServiceRetry() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(4000))
                .retryExceptions(Exception.class)
                .ignoreExceptions(IllegalArgumentException.class)
                .build();

        return Retry.of("addressService", config);
    }
}
