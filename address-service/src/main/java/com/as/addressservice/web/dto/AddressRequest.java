package com.as.addressservice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressRequest implements Serializable {
    private String id;
    private String address2;
    private String city;
    private String address1;
    private String zipCode;
    private String country;
    private String state;
    private String street;
}
