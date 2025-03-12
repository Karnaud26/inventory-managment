package com.cvs.customervendorservice.exceptions;

import lombok.Getter;

import java.util.List;


public class InvalidEntityException extends RuntimeException {

    @Getter
    private ErrorCodes errorCodes;

    @Getter
    private List<String> errors;

    public InvalidEntityException() {
        super();
    }

    public InvalidEntityException(final String message) {
        super(message);
    }

    public InvalidEntityException(final String message,final Throwable cause, final ErrorCodes errorCodes) {
        super(message, cause);
        this.errorCodes = errorCodes;
    }

    public InvalidEntityException(final String message,final ErrorCodes errorCodes) {
        super(message);
        this.errorCodes = errorCodes;
    }

    public InvalidEntityException(final String message, final ErrorCodes errorCodes, List<String> errors) {
        super(message);
        this.errorCodes = errorCodes;
        this.errors = errors;
    }

    public InvalidEntityException(final Throwable cause) {
        super(cause);
    }

    public InvalidEntityException(final Integer id) {
        super(String.valueOf(id));
    }

    public InvalidEntityException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
