package com.cvs.customervendorservice.exceptions;

import lombok.Getter;

import java.util.List;

public class EntityNotFoundException extends RuntimeException {

    @Getter
    private ErrorCodes errorCodes;

    @Getter
    private List<String> errors;

    public EntityNotFoundException(final String message, final ErrorCodes errorCodes) {
        super(message);
        this.errorCodes = errorCodes;
    }

    public EntityNotFoundException(final String message) {
        super(message);
    }

    public EntityNotFoundException(final String message, final Throwable cause, final ErrorCodes errorCodes, List<String > errors) {
        super(message, cause);
        this.errorCodes = errorCodes;
        this.errors = errors;
    }

    public EntityNotFoundException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
