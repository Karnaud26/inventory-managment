package com.cvs.customervendorservice.web.model;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressRequest implements Serializable {

    private String address2;
    private String city;
    private String address1;
    private String zipCode;
    private String country;
    private String state;
    private String street;
}
