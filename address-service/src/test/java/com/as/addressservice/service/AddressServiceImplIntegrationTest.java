package com.as.addressservice.service;

import com.as.addressservice.entities.Address;
import com.as.addressservice.exceptions.EntityNotFoundException;
import com.as.addressservice.exceptions.InvalidEntityException;
import com.as.addressservice.repository.ReadOnlyAddressRepository;
import com.as.addressservice.repository.WriteAddressRepository;
import com.as.addressservice.web.dto.AddressRequest;
import com.as.addressservice.web.dto.AddressResponse;
import config.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("integration")
class AddressServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private WriteAddressRepository writeAddressRepository;

    @Autowired
    private ReadOnlyAddressRepository readOnlyAddressRepository;

    private Address existingAddress;

    @BeforeEach
    void setUp() {
        existingAddress = Address.builder()
                .street("123 Main St")
                .address1("3456 Ave Norvège")
                .city("Springfield")
                .state("IL")
                .zipCode("62704")
                .country("USA")
                .build();

        existingAddress = writeAddressRepository.saveAndFlush(existingAddress);
    }

    // ---- findAddressById ----

    @Test
    void findAddressById_ShouldReturnPersistedAddress() {
        AddressResponse result = addressService.findAddressById(existingAddress.getId().toString());

        assertThat(result).isNotNull();
        assertThat(result.getStreet()).isEqualTo("123 Main St");
        assertThat(result.getCity()).isEqualTo("Springfield");
    }

    @Test
    void findAddressById_WithNullId_ShouldReturnNull() {
        AddressResponse result = addressService.findAddressById(null);

        assertThat(result).isNull();
    }

    @Test
    void findAddressById_WithNonExistentId_ShouldThrowEntityNotFoundException() {
        String randomId = UUID.randomUUID().toString();

        assertThrows(EntityNotFoundException.class,
                () -> addressService.findAddressById(randomId));
    }

    @Test
    void findAddressById_WithMalformedId_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> addressService.findAddressById("not-a-uuid"));
    }

    // ---- saveAddress: création ----

    @Test
    void saveAddress_WithValidNewRequest_ShouldPersistAndReturnResponse() {
        AddressRequest request = AddressRequest.builder()
                .street("999 New Street")
                .address1("Apt 4B")
                .city("Chicago")
                .state("IL")
                .zipCode("60601")
                .country("USA")
                .build();

        AddressResponse result = addressService.saveAddress(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();

        var persisted = readOnlyAddressRepository.findById(UUID.fromString(result.getId()));
        assertThat(persisted).isPresent();
        assertThat(persisted.get().getCity()).isEqualTo("Chicago");
        assertThat(persisted.get().getAddress1()).isEqualTo("Apt 4B");
    }

    @Test
    void saveAddress_MissingAddress1_ShouldThrowInvalidEntityException() {
        AddressRequest request = AddressRequest.builder()
                .street("999 New Street")
                .city("Chicago")
                .state("IL")
                .zipCode("60601")
                .country("USA")
                // address1 manquant
                .build();

        InvalidEntityException exception = assertThrows(InvalidEntityException.class,
                () -> addressService.saveAddress(request));

        assertThat(exception.getErrors()).contains("Address 1 is mandatory");
    }

    @Test
    void saveAddress_MissingZipCode_ShouldThrowInvalidEntityException() {
        AddressRequest request = AddressRequest.builder()
                .street("999 New Street")
                .address1("Apt 4B")
                .city("Chicago")
                .state("IL")
                .country("USA")
                // zipCode manquant
                .build();

        InvalidEntityException exception = assertThrows(InvalidEntityException.class,
                () -> addressService.saveAddress(request));

        assertThat(exception.getErrors()).contains("Zip code is mandatory");
    }

    @Test
    void saveAddress_WithNullRequest_ShouldThrowInvalidEntityException() {
        InvalidEntityException exception = assertThrows(InvalidEntityException.class,
                () -> addressService.saveAddress(null));

        assertThat(exception.getErrors())
                .containsExactlyInAnyOrder("Address 1 is mandatory", "Zip code is mandatory");
    }

    // ---- saveAddress: mise à jour ----

    @Test
    void saveAddress_WithExistingId_ShouldUpdateAddress() {
        AddressRequest updateRequest = AddressRequest.builder()
                .id(existingAddress.getId().toString())
                .street("Updated Street")
                .address1(existingAddress.getAddress1())
                .city("Updated City")
                .state("IL")
                .zipCode("62704")
                .country("USA")
                .build();

        AddressResponse result = addressService.saveAddress(updateRequest);

        assertThat(result.getId()).isEqualTo(existingAddress.getId().toString());
        assertThat(result.getStreet()).isEqualTo("Updated Street");
        assertThat(result.getCity()).isEqualTo("Updated City");

        var persisted = readOnlyAddressRepository.findById(existingAddress.getId());
        assertThat(persisted).isPresent();
        assertThat(persisted.get().getStreet()).isEqualTo("Updated Street");
    }

    @Test
    void saveAddress_WithNonExistentId_ShouldThrowEntityNotFoundException() {
        AddressRequest updateRequest = AddressRequest.builder()
                .id(UUID.randomUUID().toString())
                .address1("Some Address")
                .zipCode("00000")
                .build();

        assertThrows(EntityNotFoundException.class,
                () -> addressService.saveAddress(updateRequest));
    }

    // ---- deleteAddress ----

    @Test
    void deleteAddress_ShouldRemoveFromDatabase() {
        UUID id = existingAddress.getId();

        addressService.deleteAddress(id.toString());

        assertThat(readOnlyAddressRepository.findById(id)).isEmpty();
    }

    @Test
    void deleteAddress_WithNullId_ShouldDoNothing() {
        addressService.deleteAddress(null);

        // l'adresse existante ne doit pas être affectée
        assertThat(readOnlyAddressRepository.findById(existingAddress.getId())).isPresent();
    }

    @Test
    void deleteAddress_WithNonExistentId_ShouldThrowEntityNotFoundException() {
        String randomId = UUID.randomUUID().toString();

        assertThrows(EntityNotFoundException.class,
                () -> addressService.deleteAddress(randomId));
    }

    @Test
    void deleteAddress_WithMalformedId_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> addressService.deleteAddress("not-a-uuid"));
    }
}