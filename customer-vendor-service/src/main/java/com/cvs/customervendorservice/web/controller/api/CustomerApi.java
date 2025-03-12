package com.cvs.customervendorservice.web.controller.api;


import com.cvs.customervendorservice.web.dto.CustomerRequest;
import com.cvs.customervendorservice.web.dto.CustomerResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.cvs.customervendorservice.web.utils.Constants.*;

@Tag(name = "Customer Api", description = "the Customer API with documentation annotations")
@Api(value = APP_ROOT , protocols = "http, https", produces =  MediaType.APPLICATION_JSON_VALUE, tags = "Customer Api")
public interface CustomerApi {

    @Operation(summary = "Get customer details", description = "Get the customer details. The operation returns the details of the customer by customer Id.")
    @GetMapping(value = FIND_CUSTOMER_BY_ID_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The customer has been found"),
            @ApiResponse(responseCode = "204", description = "The customer was not found in the database")
    })
    ResponseEntity<CustomerResponse> getCustomerById(@PathVariable final String customerId);

    @DeleteMapping(value = DELETE_CUSTOMER_BY_ID_ENDPOINT)
    ResponseEntity<Void> deleteCustomerById(@PathVariable final String customerId);

    @Operation(summary = "Save customer infos", description = "Save the customer. The operation returns the current customer saved")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created customer"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Server Error")})
    @PostMapping(value = CREATE_CUSTOMER_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CustomerResponse> saveCustomer(@RequestPart("customer") @Valid @RequestBody final CustomerRequest customerRequest
            , @RequestPart(value = "image", required = false) final MultipartFile imageFile) throws IOException;


    @Operation(summary = "Get customer list", description = "Get the customer list. The operation returns the details of the customer.")
    @GetMapping(value = FIND_ALL_CUSTOMERS, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The customer has been found"),
            @ApiResponse(responseCode = "204", description = "The customer was not found in the database")
    })
    ResponseEntity<List<CustomerResponse>> getAllCustomers();
}
