package com.as.addressservice.web.mappers;

import com.as.addressservice.web.dto.AddressRequest;
import com.as.addressservice.web.dto.AddressResponse;
import com.as.addressservice.entities.Address;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    private ModelMapper modelMapper;
    public AddressMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Address toAddress(final AddressRequest addressRequest) {
        if (addressRequest == null) {
            throw new IllegalArgumentException("AddressRequest must not be null");
        }
        return modelMapper.map(addressRequest, Address.class);
    }

    public AddressResponse toAddressResponse(final Address address) {
        return modelMapper.map(address, AddressResponse.class);
    }

    public Address fromAddressResponseToAddress(final AddressResponse addressResponse) {
        return modelMapper.map(addressResponse, Address.class);
    }

    public void updateAddressFromAddressRequest(AddressRequest request, Address address) {
        modelMapper.map(request, address);
    }
}
