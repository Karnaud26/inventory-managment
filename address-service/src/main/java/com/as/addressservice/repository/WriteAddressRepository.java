package com.as.addressservice.repository;

import com.as.addressservice.entities.Address;
import org.springframework.data.repository.Repository;

import java.util.UUID;

@org.springframework.stereotype.Repository
public interface WriteAddressRepository extends Repository<Address, UUID> {

    <S extends Address> S save(final S entity);
    void delete(final Address entity);
}
