package com.as.addressservice.handlers;

import com.as.addressservice.exceptions.ErrorCodes;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDto {

    private Integer httpCode;

    private ErrorCodes errorCodes;

    private String message;

    private List<String > errors;

}
