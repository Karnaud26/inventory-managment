package com.cvs.customervendorservice.web.validator;

import com.cvs.customervendorservice.web.dto.CustomerRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomerValidator {

    public static List<String> validate(final CustomerRequest customerRequest){
        List<String> errors = new ArrayList<>();
        if (customerRequest == null){
            errors.add("Customer code is required");
            errors.add("Customer name is required");
            errors.add("Customer email is required");
            errors.add("Customer First name is required");
            errors.add("Customer Last name is required");
            errors.add("Customer Status is required");
            errors.addAll(AddressValidator.validate(null));
        }

        if(!StringUtils.hasLength(customerRequest.getEmail())){
            errors.add("Customer email is required");
        }

        if(!StringUtils.hasLength(customerRequest.getFirstName())){
            errors.add("Customer First name is required");
        }

        if(!StringUtils.hasLength(customerRequest.getLastName())){
            errors.add("Customer Last name is required");
        }

        if(!StringUtils.hasLength(customerRequest.getCode())){
            errors.add("Customer code is required");
        }

        if(!StringUtils.hasLength(customerRequest.getStatus())){
            errors.add("Customer Status is required");
        }

        errors.addAll(AddressValidator.validate(customerRequest.getAddressRequest()));

        return errors;
    }
}
