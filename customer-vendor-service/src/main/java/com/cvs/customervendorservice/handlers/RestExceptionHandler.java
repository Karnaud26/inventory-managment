package com.cvs.customervendorservice.handlers;

import com.cvs.customervendorservice.exceptions.AddressServiceUnavailableException;
import com.cvs.customervendorservice.exceptions.EntityAlreadyExistException;
import com.cvs.customervendorservice.exceptions.EntityNotFoundException;
import com.cvs.customervendorservice.exceptions.InvalidEntityException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handlerException(final EntityNotFoundException ex, final WebRequest webRequest){

        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        final var errorDto =  ErrorDto.builder()
                .errorCodes(ex.getErrorCodes())
                .errors(ex.getErrors())
                .httpCode(notFound.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorDto, notFound);
    }

    @ExceptionHandler(value = InvalidEntityException.class)
    public ResponseEntity<ErrorDto> handlerException(final InvalidEntityException exception, final WebRequest webRequest){

        final HttpStatus batRequest = HttpStatus.BAD_REQUEST;
        final var errorDto = ErrorDto.builder()
                .errorCodes(exception.getErrorCodes())
                .httpCode(batRequest.value())
                .errors(exception.getErrors())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorDto, batRequest);
    }

    @ExceptionHandler(value = AddressServiceUnavailableException.class)
    public ResponseEntity<ErrorDto> handlerException(final AddressServiceUnavailableException exception, final WebRequest webRequest){

        final HttpStatus batRequest = HttpStatus.BAD_REQUEST;
        final var errorDto = ErrorDto.builder()
                .httpCode(batRequest.value())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorDto, batRequest);
    }

    @ExceptionHandler(FeignException.ServiceUnavailable.class)
    public ResponseEntity<Object> handleServiceUnavailable(FeignException.ServiceUnavailable ex) {
        logger.error("Service unavailable exception: {}", ex.getMessage());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        body.put("error", "Service Unavailable");
        body.put("message", "The requested service is temporarily unavailable");
        body.put("service", extractServiceName(ex));

        // Add detailed information for logging/debugging, but not to be shown to end users
        logger.debug("Request that caused error: {}", ex.request() != null ? ex.request().url() : "Unknown");

        return new ResponseEntity<>(body, HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * Handle all other FeignExceptions
     */
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignException(FeignException ex) {
        logger.error("Feign exception: {} - {}", ex.getClass().getSimpleName(), ex.getMessage());

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        // Map Feign status codes to HTTP status
        if (ex.status() > 0) {
            status = HttpStatus.valueOf(ex.status());
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", ex.getMessage());
        body.put("service", extractServiceName(ex));

        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(value = EntityAlreadyExistException.class)
    public ResponseEntity<ErrorDto> handlerException(final EntityAlreadyExistException exception, final WebRequest webRequest){

        final HttpStatus batRequest = HttpStatus.ALREADY_REPORTED;
        final var errorDto = ErrorDto.builder()
                .errorCodes(exception.getErrorCodes())
                .httpCode(batRequest.value())
                .errors(exception.getErrors())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorDto, batRequest);
    }


    /**
     * Extract the service name from the Feign exception
     */
    private String extractServiceName(FeignException ex) {
        if (ex.request() != null) {
            String url = ex.request().url();
            // Extract service name from URL (e.g., "http://address-service/api/addresses" -> "address-service")
            if (url.contains("://")) {
                String host = url.split("://")[1].split("/")[0];
                return host.contains(":") ? host.split(":")[0] : host;
            }
        }
        return "unknown-service";
    }

}
