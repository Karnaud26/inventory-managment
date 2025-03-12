package com.cvs.customervendorservice.web.controller;

import com.cvs.customervendorservice.service.CustomerService;
import com.cvs.customervendorservice.web.controller.api.CustomerApi;
import com.cvs.customervendorservice.web.dto.CustomerRequest;
import com.cvs.customervendorservice.web.dto.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class CustomerRestController implements CustomerApi {

    private final CustomerService customerService;

    @Override
    public ResponseEntity<CustomerResponse> getCustomerById(final String customerId) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getCustomerById(customerId));
    }

    @Override
    public ResponseEntity<Void> deleteCustomerById(final String customerId) {
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<CustomerResponse> saveCustomer(final CustomerRequest customerRequest, final MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.save(customerRequest, file));
    }

    @Override
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getAllCustomers());
    }
}
