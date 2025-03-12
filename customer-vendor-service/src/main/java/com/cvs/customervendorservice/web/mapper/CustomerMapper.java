package com.cvs.customervendorservice.web.mapper;


import com.cvs.customervendorservice.entities.Customer;
import com.cvs.customervendorservice.web.dto.CustomerRequest;
import com.cvs.customervendorservice.web.dto.CustomerResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public CustomerResponse toCustomerResponse(final Customer customer) {
        return modelMapper.map(customer, CustomerResponse.class);
    }

    public List<CustomerResponse> toCustomerResponse(final List<Customer> customers) {
        return modelMapper.map(customers, List.class);
    }

    public Customer toCustomer(final CustomerRequest customerRequest) {
        return modelMapper.map(customerRequest, Customer.class);
    }
}
