package com.as.addressservice.web.mappers;

import com.as.addressservice.web.dto.AddressRequest;
import com.as.addressservice.web.dto.AddressResponse;
import com.as.addressservice.entities.Address;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public Address toAddress(final AddressRequest addressRequest) {
        return modelMapper.map(addressRequest, Address.class);
    }

    public AddressResponse toAddressResponse(final Address address) {
        return modelMapper.map(address, AddressResponse.class);
    }

    public Address fromAddressResponseToAddress(final AddressResponse addressResponse) {
        return modelMapper.map(addressResponse, Address.class);
    }
}
