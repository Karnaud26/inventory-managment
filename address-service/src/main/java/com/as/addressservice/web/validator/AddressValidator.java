package com.as.addressservice.web.validator;

import com.as.addressservice.web.dto.AddressRequest;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AddressValidator {

    public static List<String> validAddress(final AddressRequest addressRequest) {
        List<String> errors = new ArrayList<>();
        if(addressRequest == null){
            errors.add("Address 1 is mandatory");
            errors.add("Zip code is mandatory");
            return errors;
        }
        if(!StringUtils.hasLength(addressRequest.getAddress1())){
            errors.add("Address 1 is mandatory");
        }
        if(!StringUtils.hasLength(addressRequest.getZipCode())){
            errors.add("Zip code is mandatory");
        }
        return errors;

    }
}
