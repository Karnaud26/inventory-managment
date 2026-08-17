package com.as.addressservice.repository;

import com.as.addressservice.entities.Address;
import config.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("integration")
public class AddressRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private WriteAddressRepository writeAddressRepository;

    @Autowired
    private ReadOnlyAddressRepository readOnlyAddressRepository;

    private Address address1;
    private Address address2;

    @BeforeEach
    public void setUp(){
        address1 = Address.builder()
                .street("123 Main St")
                .city("New York")
                .address1("123 Main")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();
        address2 = Address.builder()
                .street("456 Broadway")
                .address1("456 Broadway")
                .city("New York")
                .state("NY")
                .zipCode("10002")
                .country("USA")
                .build();

        address1 = writeAddressRepository.saveAndFlush(address1);
        address2 = writeAddressRepository.saveAndFlush(address2);
    }

    @Test
    public void findAddressById_WithExistingId_ShouldReturnAddress(){

        Optional<Address> foundAddress = readOnlyAddressRepository.findById(address1.getId());

        assertThat(foundAddress).isPresent();
        assertThat(foundAddress.get()).usingRecursiveComparison().isEqualTo(address1);
    }

    @Test
    public void findAddressById_WithNonExistingId_ShouldReturnEmptyOptional(){
        String id = UUID.randomUUID().toString();
        Optional<Address> foundAddress = readOnlyAddressRepository.findById(UUID.fromString(id));
        assertThat(foundAddress).isEmpty();

    }

    @Test
    public void saveAddress_ShouldPersistAddressAndReturnAddress(){

        var newAddress = Address.builder()
                .street("999 New Street")
                .city("Chicago")
                .state("IL")
                .address1("999 New Street")
                .zipCode("60601")
                .country("USA")
                .build();

        Address savedAddress = writeAddressRepository.saveAndFlush(newAddress);

        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getId()).isNotNull();

        // Verify it's in the database
        Optional<Address> foundAddress = readOnlyAddressRepository.findById(savedAddress.getId());
        assertThat(foundAddress).isPresent();
        assertThat(newAddress).usingRecursiveComparison().isEqualTo(savedAddress);
        assertThat(foundAddress.get().getStreet()).isEqualTo("999 New Street");
        assertThat(foundAddress.get().getCity()).isEqualTo("Chicago");
    }

    @Test
    public void updateAddress_ShouldUpdateExistingAddressAndReturnAddress(){

        Optional<Address> existAddress = readOnlyAddressRepository.findById(address1.getId());
        existAddress.get().setStreet("Updated 999 New Street");
        existAddress.get().setCity("Updated Chicago");

        var updatedAddress = writeAddressRepository.saveAndFlush(existAddress.get());

        assertThat(updatedAddress).isNotNull();
        assertThat(updatedAddress.getId()).isEqualTo(address1.getId());
        assertThat(updatedAddress.getStreet()).isEqualTo("Updated 999 New Street");
        assertThat(updatedAddress.getCity()).isEqualTo("Updated Chicago");

        Optional<Address> foundAddress = readOnlyAddressRepository.findById(address1.getId());
        assertThat(foundAddress).isPresent();
        assertThat(foundAddress.get().getStreet()).isEqualTo("Updated 999 New Street");
        assertThat(foundAddress.get().getCity()).isEqualTo("Updated Chicago");
    }

    @Test
    public void deleteAddress_ShouldDeleteAddress(){
        writeAddressRepository.deleteById(address1.getId());

        Optional<Address> foundAddress = readOnlyAddressRepository.findById(address1.getId());

        assertThat(foundAddress).isEmpty();
    }
}
