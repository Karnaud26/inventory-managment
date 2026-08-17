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
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@ActiveProfiles(value = "test")
public class AddressServiceImplTest {

    @Mock
    private ReadOnlyAddressRepository readOnlyAddressRepository;
    @Mock
    private WriteAddressRepository writeAddressRepository;
    @Mock
    private AddressMapper addressMapper;
    @InjectMocks
    private AddressServiceImpl addressService;


    private AddressResponse addressResponse;

    private AddressRequest validAddressRequest;
    private Address existingAddress;
    private Address savedAddress;
    private AddressResponse expectedResponse;
    private UUID validId;
    private LocalDateTime now;

    @BeforeEach
    public void setUp(){
        validId = UUID.randomUUID();
        now = LocalDateTime.now();

        // Setup valid address request
        validAddressRequest = AddressRequest.builder()
                .street("123 Main St")
                .address1("3456 Ave Norvege")
                .city("Springfield")
                .state("IL")
                .zipCode("62704")
                .country("USA")
                .build();
        // Setup existing address
        existingAddress = Address.builder()
                .street("123 Main St")
                .city("Metropolis")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .id(validId)
                .createBy("user1")
                .modifiedBy("user1")
                .createdDate(Timestamp.valueOf(now))
                .lastModifiedDate(Timestamp.valueOf(now))
            .build();

        // Setup saved address (could be either new or updated)

        savedAddress =  Address.builder()
                .id(UUID.fromString(String.valueOf(validId)))
                .street("123 Main St")
                .address1("3456 Ave Norvege")
                .city("Springfield")
                .state("IL")
                .zipCode("62704")
                .country("USA")
                .createBy("user1")
                .modifiedBy("user1")
                .createdDate(Timestamp.valueOf(now))
                .lastModifiedDate(Timestamp.valueOf(now))
                .build();

        // Setup expected response
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
        when(readOnlyAddressRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(existingAddress));
        when(addressMapper.toAddressResponse(existingAddress)).thenReturn(addressResponse);

        // when
        AddressResponse result = addressService.findAddressById(id);

        // then
        assertThat(addressResponse).usingRecursiveComparison().isEqualTo(result);
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

        when(readOnlyAddressRepository.findById(validId)).thenReturn(Optional.of(existingAddress));
        when(addressMapper.toAddressResponse(existingAddress)).thenReturn(addressResponse);
        doNothing().when(writeAddressRepository).deleteById(existingAddress.getId());

        addressService.deleteAddress(validIdString);

        verify(readOnlyAddressRepository, times(1)).findById(parsedUuid);
        verify(addressMapper, times(1)).toAddressResponse(existingAddress);
        verify(writeAddressRepository, times(1)).deleteById(existingAddress.getId());
    }

    @Test
    public void deleteAddress_WithNullId_ShouldReturnEarly() {
        // when
        addressService.deleteAddress(null);

        // then
        verify(readOnlyAddressRepository, never()).findById(any());
        verify(writeAddressRepository, never()).deleteById(any());
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
        verify(writeAddressRepository, never()).deleteById(any());
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
        verify(writeAddressRepository, never()).deleteById(any());
    }

    @Test
    public void deleteAddressById_WhenAddressNotFound_ShouldThrowEntityNotFoundException(){
        String id = "a8ddcba3-d0e9-4160-85b6-18d994322974";
        when(readOnlyAddressRepository.findById(UUID.fromString(id))).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            addressService.deleteAddress(id);
        });

        assertThat(exception.getMessage()).isEqualTo("The address provided with the identifier Id=" + id + " does not exist");
        AssertionsForClassTypes.assertThatThrownBy(()->addressService.deleteAddress(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void saveAddress_WithValidRequest_ShouldReturnAddressResponse() {
        // given
        try (MockedStatic<AddressValidator> addressValidatorMock = mockStatic(AddressValidator.class)) {
            addressValidatorMock.when(() -> AddressValidator.validAddress(validAddressRequest))
                    .thenReturn(new ArrayList<>());

            when(addressMapper.toAddress(validAddressRequest)).thenReturn(existingAddress);
            when(writeAddressRepository.saveAndFlush(existingAddress)).thenReturn(savedAddress);
            when(addressMapper.toAddressResponse(savedAddress)).thenReturn(addressResponse);

            // when
            AddressResponse result = addressService.saveAddress(validAddressRequest);

            // then
            assertThat(result).isNotNull();
            assertThat(addressResponse).usingRecursiveComparison().isEqualTo(result);

            verify(addressMapper, times(1)).toAddress(validAddressRequest);
            verify(writeAddressRepository, times(1)).saveAndFlush(existingAddress);
            verify(addressMapper, times(1)).toAddressResponse(savedAddress);
        }
    }

    @Test
    public void saveAddress_WithInvalidRequest_ShouldThrowInvalidEntityException() {
        // given
        List<String> errorMessages = Arrays.asList("City is required", "ZipCode is invalid");

        try (MockedStatic<AddressValidator> addressValidatorMock = mockStatic(AddressValidator.class)) {
            addressValidatorMock.when(() -> AddressValidator.validAddress(validAddressRequest))
                    .thenReturn(errorMessages);

            // when & then
            InvalidEntityException exception = assertThrows(InvalidEntityException.class, () -> {
                addressService.saveAddress(validAddressRequest);
            });

            assertThat(exception.getMessage()).isEqualTo("The Address is not valid");
            assertThat(exception.getErrorCodes()).isEqualTo(ErrorCodes.ADDRESS_NOT_VALID);
            assertThat(exception.getErrors()).isExactlyInstanceOf(errorMessages.getClass());

            verify(addressMapper, never()).toAddress(any());
            verify(writeAddressRepository, never()).saveAndFlush(any());
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
            verify(writeAddressRepository, never()).saveAndFlush(any());
            verify(addressMapper, never()).toAddressResponse(any());
        }
    }

    @Test
    public void saveAddress_WithRepositoryException_ShouldPropagateException() {
        // given
        RuntimeException dbException = new RuntimeException("Database error");

        try (MockedStatic<AddressValidator> addressValidatorMock = mockStatic(AddressValidator.class)) {
            addressValidatorMock.when(() -> AddressValidator.validAddress(validAddressRequest))
                    .thenReturn(new ArrayList<>());

            when(addressMapper.toAddress(validAddressRequest)).thenReturn(existingAddress);
            when(writeAddressRepository.saveAndFlush(existingAddress)).thenThrow(dbException);

            // when & then
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                addressService.saveAddress(validAddressRequest);
            });

            assertThat(exception).isSameAs(dbException);

            verify(addressMapper, times(1)).toAddress(validAddressRequest);
            verify(writeAddressRepository, times(1)).saveAndFlush(existingAddress);
            verify(addressMapper, never()).toAddressResponse(any());
        }
    }
}
