package com.as.addressservice.service;

import com.as.addressservice.entities.Address;
import com.as.addressservice.exceptions.EntityNotFoundException;
import com.as.addressservice.exceptions.ErrorCodes;
import com.as.addressservice.exceptions.InvalidEntityException;
import com.as.addressservice.repository.ReadOnlyAddressRepository;
import com.as.addressservice.repository.WriteAddressRepository;
import com.as.addressservice.web.dto.AddressRequest;
import com.as.addressservice.web.dto.AddressResponse;
import com.as.addressservice.web.mappers.AddressMapper;
import com.as.addressservice.web.validator.AddressValidator;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "test")
public class AddressServiceImplTest {

    @Mock
    private ReadOnlyAddressRepository readOnlyAddressRepository;
    @Mock
    private WriteAddressRepository writeAddressRepository;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private Logger logger;
    @InjectMocks
    private AddressServiceImpl addressService;

    private final UUID validId = UUID.randomUUID();
    private AddressResponse addressResponse;
    private AddressRequest addressRequest;
    private Address address;

    @BeforeEach
    public void setUp(){

        address = Address.builder()
                .street("123 Main St")
                .address1("3456 Ave Norvege")
                .city("Springfield")
                .state("IL")
                .zipCode("62704")
                .country("USA")
                .build();
        addressRequest =  AddressRequest.builder()
                .id(String.valueOf(validId))
                .street("123 Main St")
                .address1("3456 Ave Norvege")
                .city("Springfield")
                .state("IL")
                .zipCode("62704")
                .country("USA")
                .build();

        addressResponse = AddressResponse.builder()
                .id(String.valueOf(validId))
                .street("123 Main St")
                .address1("3456 Ave Norvege")
                .city("Springfield")
                .state("IL")
                .zipCode("62704")
                .country("USA")
                .build();
    }
    @Test
    public void shouldFindAddressById(){
        // given
        String id = "a8ddcba3-d0e9-4160-85b6-18d994322976";
        when(readOnlyAddressRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(address));
        when(addressMapper.toAddressResponse(address)).thenReturn(addressResponse);

        //when
        AddressResponse result = addressService.findAddressById(id);

        //then
        assertThat(addressRequest).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    public void getAddressById_WhenAddressNotFound_ShouldThrowException(){
        String id = "a8ddcba3-d0e9-4160-85b6-18d994322976";
        when(readOnlyAddressRepository.findById(UUID.fromString(id))).thenReturn(Optional.empty());
        AssertionsForClassTypes.assertThatThrownBy(() -> addressService.findAddressById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("The address provided with the identifier Id=" + id + " does not exist");
    }

    @Test
    public void deleteAddress_WithValidId_ShouldDeleteAddress() {
        // given
        String validIdString = validId.toString();
        UUID parsedUuid = UUID.fromString(validIdString);

        when(readOnlyAddressRepository.findById(validId)).thenReturn(Optional.of(address));
        when(addressMapper.toAddressResponse(address)).thenReturn(addressResponse);
        when(addressMapper.fromAddressResponseToAddress(addressResponse)).thenReturn(address);
        doNothing().when(writeAddressRepository).delete(address);

        // when
        addressService.deleteAddress(validIdString);

        // then
        verify(readOnlyAddressRepository, times(1)).findById(parsedUuid);
        verify(addressMapper, times(1)).toAddressResponse(address);
        verify(addressMapper, times(1)).fromAddressResponseToAddress(addressResponse);
        verify(writeAddressRepository, times(1)).delete(address);
    }

    @Test
    public void deleteAddress_WithNullId_ShouldReturnEarly() {
        // when
        addressService.deleteAddress(null);

        // then
        verify(readOnlyAddressRepository, never()).findById(any());
        verify(writeAddressRepository, never()).delete(any());
    }

    @Test
    public void deleteAddress_WithNonExistentId_ShouldThrowEntityNotFoundException() {
        // given
        UUID nonExistentId = UUID.randomUUID();
        when(readOnlyAddressRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // when & then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            addressService.deleteAddress(nonExistentId.toString());
        });

        assertEquals("The address provided with the identifier Id=" + nonExistentId + " does not exist",
                exception.getMessage());
        assertEquals(ErrorCodes.ADDRESS_NOT_FOUND, exception.getErrorCodes());

        verify(readOnlyAddressRepository, times(1)).findById(nonExistentId);
        verify(writeAddressRepository, never()).delete(any());
    }

    @Test
    public void deleteAddress_WithInvalidUUID_ShouldHandleIllegalArgumentException() {
        // given
        String invalidId = "not-a-uuid";

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            addressService.deleteAddress(invalidId);
        });

        // No specific verification on exception message as it's from UUID.fromString()
        verify(readOnlyAddressRepository, never()).findById(any());
        verify(writeAddressRepository, never()).delete(any());
    }

    @Test
    public void deleteAddressById_WhenAddressNotFound_ShouldThrowException(){
        String id = "a8ddcba3-d0e9-4160-85b6-18d994322974";
        when(readOnlyAddressRepository.findById(UUID.fromString(id))).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            addressService.deleteAddress(id);
        });

        assertThat(exception.getMessage()).isEqualTo("The address provided with the identifier Id=" + id + " does not exist");
        /* AssertionsForClassTypes.assertThatThrownBy(()->addressService.deleteAddress(id))
                .isInstanceOf(EntityNotFoundException.class);*/
    }

    @Test
    public void saveAddress_WithValidRequest_ShouldReturnAddressResponse() {
        // given
        // Mock the validator to return empty error list for valid request
        try (MockedStatic<AddressValidator> addressValidatorMock = mockStatic(AddressValidator.class)) {
            addressValidatorMock.when(() -> AddressValidator.validAddress(addressRequest))
                    .thenReturn(new ArrayList<>());

            when(addressMapper.toAddress(addressRequest)).thenReturn(address);
            when(writeAddressRepository.save(address)).thenReturn(address);
            when(addressMapper.toAddressResponse(address)).thenReturn(addressResponse);

            // when
            AddressResponse result = addressService.saveAddress(addressRequest);

            // then
            assertThat(result).isNotNull();
            //assertThat(result.getId()).isEqualTo("a8ddcba3-d0e9-4160-85b6-18d994322976");
            //assertThat(result.getStreet()).isEqualTo("123 Main St");
            assertThat(addressRequest).usingRecursiveComparison().isEqualTo(result);

            verify(addressMapper, times(1)).toAddress(addressRequest);
            verify(writeAddressRepository, times(1)).save(address);
            verify(addressMapper, times(1)).toAddressResponse(address);
        }
    }

    @Test
    public void saveAddress_WithInvalidRequest_ShouldThrowInvalidEntityException() {
        // given
        List<String> errorMessages = Arrays.asList("City is required", "ZipCode is invalid");

        // Mock the validator to return error messages for invalid request
        try (MockedStatic<AddressValidator> addressValidatorMock = mockStatic(AddressValidator.class)) {
            addressValidatorMock.when(() -> AddressValidator.validAddress(addressRequest))
                    .thenReturn(errorMessages);

            // when & then
            InvalidEntityException exception = assertThrows(InvalidEntityException.class, () -> {
                addressService.saveAddress(addressRequest);
            });

            assertThat(exception.getMessage()).isEqualTo("The Address is not valid");
            assertThat(exception.getErrorCodes()).isEqualTo(ErrorCodes.ADDRESS_NOT_VALID);
            assertThat(exception.getErrors()).isExactlyInstanceOf(errorMessages.getClass());

            verify(addressMapper, never()).toAddress(any());
            verify(writeAddressRepository, never()).save(any());
            verify(addressMapper, never()).toAddressResponse(any());
        }
    }

    @Test
    public void saveAddress_WithNullRequest_ShouldHandleNullPointerGracefully() {
        // This test depends on how AddressValidator handles null - adjust as needed
        // given
        List<String> errorMessages = Arrays.asList("Address cannot be null");

        try (MockedStatic<AddressValidator> addressValidatorMock = mockStatic(AddressValidator.class)) {
            addressValidatorMock.when(() -> AddressValidator.validAddress(null))
                    .thenReturn(errorMessages);

            // when & then
            InvalidEntityException exception = assertThrows(InvalidEntityException.class, () -> {
                addressService.saveAddress(null);
            });

            assertThat(exception.getMessage()).isEqualTo("The Address is not valid");
            assertThat(exception.getErrorCodes()).isEqualTo(ErrorCodes.ADDRESS_NOT_VALID);

            verify(addressMapper, never()).toAddress(any());
            verify(writeAddressRepository, never()).save(any());
            verify(addressMapper, never()).toAddressResponse(any());
        }
    }

    @Test
    public void saveAddress_WithRepositoryException_ShouldPropagateException() {
        // given
        RuntimeException dbException = new RuntimeException("Database error");

        try (MockedStatic<AddressValidator> addressValidatorMock = mockStatic(AddressValidator.class)) {
            addressValidatorMock.when(() -> AddressValidator.validAddress(addressRequest))
                    .thenReturn(new ArrayList<>());

            when(addressMapper.toAddress(addressRequest)).thenReturn(address);
            when(writeAddressRepository.save(address)).thenThrow(dbException);

            // when & then
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                addressService.saveAddress(addressRequest);
            });

            assertThat(exception).isSameAs(dbException);

            verify(addressMapper, times(1)).toAddress(addressRequest);
            verify(writeAddressRepository, times(1)).save(address);
            verify(addressMapper, never()).toAddressResponse(any());
        }
    }
}
