package com.cvs.customervendorservice.repository;

import com.cvs.customervendorservice.entities.Customer;
import org.springframework.data.repository.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Repository
public interface ReadOnlyCustomerRepository extends Repository<Customer,UUID> {

    Optional<Customer> findById(final UUID id);
    Optional<Customer> findCustomerByCode(final String code);
    List<Customer> findAll();
    boolean existsCustomerByCode(final String code);
    boolean existsCustomerByEmail(final String email);
    boolean existsCustomerById(final UUID id);
}
