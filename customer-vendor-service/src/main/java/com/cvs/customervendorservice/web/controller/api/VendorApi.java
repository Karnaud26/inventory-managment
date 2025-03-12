package com.cvs.customervendorservice.web.controller.api;

import com.cvs.customervendorservice.web.dto.CustomerResponse;
import com.cvs.customervendorservice.web.dto.VendorRequest;
import io.swagger.annotations.Api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.cvs.customervendorservice.web.utils.Constants.*;

@Tag(name = "Vendor Api", description = "the Vendor API with documentation annotations")
@Api(value = APP_ROOT , protocols = "http, https", produces =  MediaType.APPLICATION_JSON_VALUE, tags = "Vendor Api")
public interface VendorApi {

    @PostMapping(value = CREATE_CUSTOMER_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CustomerResponse> save(@Validated @RequestBody final VendorRequest vendorRequest, final @RequestParam("file") MultipartFile imageFile);

    @GetMapping(value = FIND_ALL_CUSTOMERS, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<CustomerResponse>> findAll();

}
