package com.cvs.customervendorservice.feign;

import com.cvs.customervendorservice.web.model.AddressRequest;
import com.cvs.customervendorservice.web.model.AddressResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddressFallbackFactory implements AddressClient {

    private static final Logger logger = LoggerFactory.getLogger(AddressFallbackFactory.class);

    @Override
    public ResponseEntity<AddressResponse> saveAddress(final AddressRequest addressRequest) {
        logger.error("Fallback for saveAddress called with addressRequest: {}", addressRequest);
        AddressResponse fallbackResponse = getDefaultAddress();
        return ResponseEntity.status(HttpStatus.OK).body(fallbackResponse);
    }

    @Override
    public ResponseEntity<AddressResponse> findAddressById(final String  id) {
        logger.error("Fallback for findAddressById called for id: {}", id);
        AddressResponse fallbackResponse = getDefaultAddress();
        fallbackResponse.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(fallbackResponse);
    }

    @Override
    public ResponseEntity<Void> deleteAddressById(final String id) {
        logger.error("Fallback for deleteAddressById called for id: {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private AddressResponse getDefaultAddress(){
        return AddressResponse.builder()
                .address1("Fallback Address 1")
                .zipCode("00000")
                .country("Circuit Breaker Land")
                .street("Fallback Street")
                .city("Fallback City")
                .address2("Fallback Address 2")
                .state("FB")
                .build();
    }
}
