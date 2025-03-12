package com.as.addressservice.service;

import com.as.addressservice.web.dto.AddressRequest;
import com.as.addressservice.web.dto.AddressResponse;

public interface AddressService {

    AddressResponse findAddressById(final String Id);
    void deleteAddress(final String Id);
    AddressResponse saveAddress(final AddressRequest addressRequest);
}
