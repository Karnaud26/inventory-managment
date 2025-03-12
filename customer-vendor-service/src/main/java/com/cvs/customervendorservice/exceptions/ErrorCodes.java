package com.cvs.customervendorservice.exceptions;

public enum ErrorCodes {

    CUSTOMER_NOT_FOUND(1000),
    CUSTOMER_NOT_VALID(1001),
    VENDOR_NOT_FOUND(1002),
    CUSTOMER_ALREADY_EXIST(1004),
    VENDOR_ALREADY_EXIST(1005),
    VENDOR_NOT_VALID(1003),
    SERVICE_UNAVAILABLE(500);


    private final int code;
    ErrorCodes(int code) {
        this.code = code;
    }
    public final int getCode() {
        return code;
    }
}
