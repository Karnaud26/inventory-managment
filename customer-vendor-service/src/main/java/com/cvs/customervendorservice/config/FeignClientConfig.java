package com.cvs.customervendorservice.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Configuration
public class FeignClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    public static class CustomErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            switch (response.status()) {
                case 400 -> {
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request to downstream service");
                }
                case 404 -> {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found in downstream service");
                }
                case 500 -> {
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Downstream service error");
                }
                default -> {
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()), "Error communicating with downstream service");
                }
            }
        }
    }
}
