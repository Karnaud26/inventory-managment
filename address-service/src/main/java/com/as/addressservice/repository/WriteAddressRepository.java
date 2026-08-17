package com.as.addressservice.repository;

import com.as.addressservice.entities.Address;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Repository
public interface WriteAddressRepository extends Repository<Address, UUID> {

    Address saveAndFlush(final Address address);
    void deleteById(final UUID id);
}
