package com.cvs.customervendorservice.repository;

import com.cvs.customervendorservice.entities.Vendor;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Repository
public interface ReadOnlyVendorRepository extends Repository<Vendor, UUID> {

    Optional<Vendor> findById(final UUID id);
    Optional<Vendor> findByCode(final String code);
}
