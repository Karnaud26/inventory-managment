package com.cvs.customervendorservice.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.UUID;

@Configuration
public class FeignClientAdvancedConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestIdInterceptor() {
        return template -> {
            // Add correlation ID to trace requests across services
            String correlationId = UUID.randomUUID().toString();
            template.header("X-Correlation-ID", correlationId);
            template.header("Content-Type", "application/json");
        };
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new DetailedErrorDecoder();
    }

    public static class DetailedErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder defaultDecoder = new Default();

        @Override
        public Exception decode(String methodKey, Response response) {
            try {
                // Log the error details
                System.err.println("Error Response: " + response.status() + " for " + methodKey);
                if (response.body() != null) {
                    System.err.println("Error Body: " + new String(response.body().asInputStream().readAllBytes()));
                }
            } catch (IOException e) {
                System.err.println("Failed to read error response body");
            }

            return defaultDecoder.decode(methodKey, response);
        }
    }
}
