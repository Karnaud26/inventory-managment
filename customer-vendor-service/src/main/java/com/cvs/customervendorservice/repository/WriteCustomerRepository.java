package com.cvs.customervendorservice.repository;

import com.cvs.customervendorservice.entities.Customer;
import org.springframework.data.repository.Repository;

import java.util.UUID;

@org.springframework.stereotype.Repository
public interface WriteCustomerRepository extends Repository<Customer, UUID> {
    <S extends Customer> S save(final S entity);
    void delete(final Customer entity);

    void deleteById(final UUID id);
}
