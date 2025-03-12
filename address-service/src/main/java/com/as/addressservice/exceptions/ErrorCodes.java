package com.as.addressservice.exceptions;

public enum ErrorCodes {

    ADDRESS_NOT_FOUND(1000),
   ADDRESS_NOT_VALID(1001);

    private final int code;
    ErrorCodes(int code) {
        this.code = code;
    }
    public final int getCode() {
        return code;
    }
}
