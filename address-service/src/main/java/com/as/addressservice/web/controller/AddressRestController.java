package com.as.addressservice.web.controller;

import com.as.addressservice.service.AddressService;
import com.as.addressservice.web.controller.api.AddressApi;
import com.as.addressservice.web.dto.AddressRequest;
import com.as.addressservice.web.dto.AddressResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AddressRestController implements AddressApi {

    private final AddressService addressService;
    @Override
    public ResponseEntity<AddressResponse> findAddressById(final String id) {
        return ResponseEntity.status(HttpStatus.OK).body(addressService.findAddressById(id));
    }

    @Override
    public ResponseEntity<Void> deleteAddressById(final String id) {
        addressService.deleteAddress(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<AddressResponse> saveAddress(final AddressRequest addressRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.saveAddress(addressRequest));
    }

}
