package com.cvs.customervendorservice.exceptions;

import lombok.Getter;

public class AddressServiceUnavailableException  extends RuntimeException {

    @Getter
    private ErrorCodes errorCodes;

    public AddressServiceUnavailableException(String message) {
        super(message);
    }

    public AddressServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddressServiceUnavailableException(String message, ErrorCodes errorCodes ,Throwable cause) {
        super(message, cause);
        this.errorCodes = errorCodes;
    }

}
