package com.cvs.customervendorservice.web.validator;

import com.cvs.customervendorservice.web.dto.VendorRequest;
import com.cvs.customervendorservice.web.model.AddressRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VendorValidator {

    public List<String> validate(final VendorRequest vendorRequest){
        List<String> errors = new ArrayList<>();
        if(vendorRequest == null){
            errors.add("Code is required");
            errors.add("Vendor Type is required");
            errors.add("Vendor Name is required");
            errors.addAll(AddressValidator.validate(null));
        }

        if(!StringUtils.hasLength(vendorRequest.getVendorName())){
            errors.add("vendorName is required");
        }

        if(!StringUtils.hasLength(vendorRequest.getCustomerType())){
            errors.add("Vendor Type is required");
        }

        if(!StringUtils.hasLength(vendorRequest.getEmail())){
            errors.add("Email is required");
        }

        if(!StringUtils.hasLength(vendorRequest.getCode())){
            errors.add("Vendor Code is required");
        }
        errors.addAll(AddressValidator.validate(vendorRequest.getAddressRequest()));

        return errors;
    }
}
