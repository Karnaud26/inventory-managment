package com.as.addressservice.repository;

import com.as.addressservice.entities.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class AddressRepositoryTest {

    @Autowired
    ReadOnlyAddressRepository readOnlyAddressRepository;

    @Autowired
    WriteAddressRepository writeAddressRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setup(){
        System.out.println("-------------------------------------------------------");
        writeAddressRepository.save(Address.builder()
                        .address1("3456 Ave Norvege")
                        .state("Canada")
                        .zipCode("12345")
                        .country("United Kingdom")
                        .city("United Kingdom")
                .build());
        writeAddressRepository.save(Address.builder()
                .address1("3446 Ave Norh")
                .state("USA")
                .zipCode("12245")
                .country("United State")
                .city("Seatle")
                .build());
        writeAddressRepository.save(Address.builder()
                .address1("3456 Ave Norvege")
                .state("USA")
                .zipCode("12145")
                .country("United State")
                .city("DC")
                .build());

    }

    @Test
    public void whenFindById_thenReturnAddress(){
        //given
        Address address = Address.builder()
                .street("123 Main St")
                .address1("3456 Ave Norvege")
                .city("Springfield")
                .state("IL")
                .zipCode("62704")
                .country("USA")
                .build();

        entityManager.persistAndFlush(address);

        //when
        Optional<Address> found = readOnlyAddressRepository.findById(address.getId());

        //then
        assertThat(found).isPresent();
        assertThat(address).usingRecursiveComparison().ignoringFields("id").isEqualTo(found.get());
    }

    @Test
    public void whenSave_thenAddressIsPersisted(){

        //given
        Address address = Address.builder()
                .street("123 Main St")
                .address1("3456 Ave Norvege")
                .city("Springfield")
                .state("IL")
                .zipCode("62704")
                .country("USA")
                .build();
        //when
        Address savedAddress = writeAddressRepository.save(address);

        //then
        Address foundAddress = entityManager.find(Address.class, savedAddress.getId());

        assertThat(foundAddress).isNotNull();
        assertThat(foundAddress).isEqualTo(savedAddress);
        assertThat(foundAddress.getStreet()).isEqualTo(savedAddress.getStreet());

    }

    @Test
    public void whenDelete_thenAddressIsDeleted(){
        //given
        Address address = Address.builder()
                .street("123 Main St")
                .address1("3456 Ave Norvege")
                .city("Springfield")
                .state("IL")
                .zipCode("62704")
                .country("USA")
                .build();

        entityManager.persistAndFlush(address);
        UUID addressId = address.getId();

        //when

        writeAddressRepository.delete(address);

        //then
        Address foundAddress = entityManager.find(Address.class, addressId);
        assertThat(foundAddress).isNull();

    }
}
