package com.cvs.customervendorservice.service;

import com.cvs.customervendorservice.web.dto.CustomerRequest;
import com.cvs.customervendorservice.web.dto.CustomerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CustomerService {

    List<CustomerResponse> getAllCustomers();
    CustomerResponse getCustomerById(final String customerId);
    CustomerResponse getCustomerByCode(final String code);
    CustomerResponse save(final CustomerRequest customerRequest, final MultipartFile imageFile) throws IOException;
    void deleteCustomerById(final String customerId);

}
