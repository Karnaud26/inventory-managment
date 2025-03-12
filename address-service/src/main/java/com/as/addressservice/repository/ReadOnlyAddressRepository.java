package com.as.addressservice.repository;

import com.as.addressservice.entities.Address;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Repository
public interface ReadOnlyAddressRepository extends Repository<Address, UUID> {
    Optional<Address> findById(final UUID id);
    boolean existsById(final UUID id);
}
