package com.as.addressservice.handlers;

import com.as.addressservice.exceptions.EntityNotFoundException;

import com.as.addressservice.exceptions.InvalidEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

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

}
