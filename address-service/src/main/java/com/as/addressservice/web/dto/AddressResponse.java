package com.as.addressservice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse implements Serializable {

    private String Id;
    private String address2;
    private String city;
    private String address1;
    private String zipCode;
    private String country;
    private String state;
    private String street;
}
