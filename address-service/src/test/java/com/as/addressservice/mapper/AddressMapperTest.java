package com.as.addressservice.mapper;

import com.as.addressservice.entities.Address;
import com.as.addressservice.web.dto.AddressRequest;
import com.as.addressservice.web.dto.AddressResponse;
import com.as.addressservice.web.mappers.AddressMapper;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = "test")
public class AddressMapperTest {

    private final AddressMapper addressMapper = new AddressMapper();
    private AddressRequest addressRequest;
    private AddressResponse addressResponse;
    private Address address;

    @Before
    public void setup() {
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
        AssertionsForClassTypes.assertThat(address).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    public void shouldNotMapNullAddressRequestToAddress(){
        AssertionsForClassTypes.assertThatThrownBy(
                () -> addressMapper.toAddress(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
