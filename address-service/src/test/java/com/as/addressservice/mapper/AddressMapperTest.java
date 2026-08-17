package com.as.addressservice.mapper;

import com.as.addressservice.entities.Address;
import com.as.addressservice.web.dto.AddressRequest;
import com.as.addressservice.web.dto.AddressResponse;
import com.as.addressservice.web.mappers.AddressMapper;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = "test")
@ExtendWith(MockitoExtension.class)
public class AddressMapperTest {

    private final ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private AddressMapper addressMapper;
    //private final AddressMapper addressMapper = new AddressMapper();
    private AddressRequest addressRequest;
    private AddressResponse addressResponse;
    private Address address;

    @BeforeEach
    public void setup() {

        addressMapper = new AddressMapper(modelMapper);
        address = Address.builder()
                .street("123 Main St")
                .address1("3456 Ave Norvège")
                .city("Springfield")
                .state("IL")
                .zipCode("62704")
                .country("USA")
                .build();
        addressRequest =  AddressRequest.builder()
                .street("123 Main St")
                .address1("3456 Ave Norvège")
                .city("Springfield")
                .state("IL")
                .zipCode("62704")
                .country("USA")
                .build();

        addressResponse =  AddressResponse.builder()
                .street("123 Main St")
                .address1("3456 Ave Norvège")
                .city("Springfield")
                .state("IL")
                .zipCode("62704")
                .country("USA")
                .build();
    }
    @Test
    public void shouldMapAddressRequestToAddress(){

        Address result = addressMapper.toAddress(addressRequest);
        AssertionsForClassTypes.assertThat(address).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    public void shouldMapAddressToAddressResponse(){
        AddressResponse result = addressMapper.toAddressResponse(address);
        AssertionsForClassTypes.assertThat(addressResponse).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    public void shouldNotMapNullAddressToAddressResponse(){
        AssertionsForClassTypes.assertThatThrownBy(
                () -> addressMapper.toAddressResponse(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldMapAddressResponseToAddress(){
        Address result = addressMapper.fromAddressResponseToAddress(addressResponse);
        AssertionsForClassTypes.assertThat(address).isEqualTo(result);
    }

    @Test
    public void shouldNotMapNullAddressRequestToAddress(){
        AssertionsForClassTypes.assertThatThrownBy(
                () -> addressMapper.toAddress(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
