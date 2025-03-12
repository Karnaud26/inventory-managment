package com.cvs.customervendorservice.exceptions;

import lombok.Getter;

import java.util.List;

public class EntityAlreadyExistException extends RuntimeException{

    @Getter
    private ErrorCodes errorCodes;

    @Getter
    private List<String> errors;

    public EntityAlreadyExistException() {
        super();
    }

    public EntityAlreadyExistException(final String message) {
        super(message);
    }

    public EntityAlreadyExistException(final String message, final ErrorCodes errorCodes) {
        super(message);
        this.errorCodes = errorCodes;
    }

    public EntityAlreadyExistException(final String message, final ErrorCodes errorCodes, List<String> errors) {
        super(message);
        this.errorCodes = errorCodes;
        this.errors = errors;
    }

    public EntityAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EntityAlreadyExistException(final String message, final Throwable cause, final ErrorCodes errorCodes) {
        super(message, cause);
        this.errorCodes = errorCodes;
    }

    public EntityAlreadyExistException(final Throwable cause) {
        super(cause);
    }

    public EntityAlreadyExistException(final Integer id) {
        super(String.valueOf(id));
    }

    public EntityAlreadyExistException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
