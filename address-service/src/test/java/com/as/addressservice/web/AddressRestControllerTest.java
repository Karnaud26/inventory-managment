package com.as.addressservice.web;

import com.as.addressservice.exceptions.EntityNotFoundException;
import com.as.addressservice.exceptions.ErrorCodes;
import com.as.addressservice.exceptions.InvalidEntityException;
import com.as.addressservice.service.AddressService;
import com.as.addressservice.web.controller.AddressRestController;
import com.as.addressservice.web.dto.AddressRequest;
import com.as.addressservice.web.dto.AddressResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AddressRestControllerTest {

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressRestController addressRestController;

    private AddressRequest addressRequest;
    private AddressResponse addressResponse;
    private String validId;

    @BeforeEach
    void setUp(){
        validId = "550e8400-e29b-41d4-a716-446655440000"; // Example UUID

        addressRequest = AddressRequest.builder()
                .street("123 Test Street")
                .address1("123")
                .city("Test City")
                .state("TS")
                .zipCode("12345")
                .country("Test Country")
                .build();
        addressResponse =  AddressResponse.builder()
                .id(validId)
                .street("123 Test Street")
                .address1("123")
                .city("Test City")
                .state("TS")
                .zipCode("12345")
                .country("Test Country")
                .build();
    }

    @Test
    void findAddressById_ShouldReturnAddress(){

        when(addressService.findAddressById(validId)).thenReturn(addressResponse);

        ResponseEntity<AddressResponse> response = addressRestController.findAddressById(validId);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isEqualTo(addressResponse);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(addressService, times(1)).findAddressById(validId);
    }

    @Test
    public void deleteAddressById_ShouldReturnNoContent(){
        doNothing().when(addressService).deleteAddress(validId);

        ResponseEntity<Void> response = addressRestController.deleteAddressById(validId);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        verify(addressService, times(1)).deleteAddress(validId);
    }


    @Test
    public void saveAddress_ShouldReturnCreatedAddressResponse(){

        when(addressService.saveAddress(addressRequest)).thenReturn(addressResponse);

        ResponseEntity<AddressResponse> response = addressRestController.saveAddress(addressRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(addressResponse);

        verify(addressService, times(1)).saveAddress(addressRequest);
    }

    @Test
    public void findAddressById_WhenServiceThrowsException_ShouldPropagateException(){

        EntityNotFoundException exception = new EntityNotFoundException(
                "The address provided with the identifier Id=\" + validId + \" does not exist", ErrorCodes.ADDRESS_NOT_FOUND
        );

        when(addressService.findAddressById(validId)).thenThrow(exception);

        assertThrows(EntityNotFoundException.class, () -> {
            addressRestController.findAddressById(validId);
        });

        verify(addressService, times(1)).findAddressById(validId);
    }

    @Test
    public void saveAddress_WhenServiceThrowsException_ShouldPropagateException(){
        InvalidEntityException exception = new InvalidEntityException(
                "Invalid address", ErrorCodes.ADDRESS_NOT_VALID
        );

        when(addressService.saveAddress(addressRequest)).thenThrow(exception);
        assertThrows(InvalidEntityException.class, () -> {
            addressRestController.saveAddress(addressRequest);
        });
        verify(addressService, times(1)).saveAddress(addressRequest);
    }
}
