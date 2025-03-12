package com.cvs.customervendorservice.web.mapper;

import com.cvs.customervendorservice.entities.Customer;
import com.cvs.customervendorservice.entities.Vendor;
import com.cvs.customervendorservice.web.dto.VendorRequest;
import com.cvs.customervendorservice.web.dto.VendorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VendorMapper {

    private final ModelMapper mapper = new ModelMapper();

    public VendorResponse toVendorResponse(final Customer customer) {
        return mapper.map(customer, VendorResponse.class);
    }

     public List<VendorResponse> toVendorResponse(final List<Customer> customers) {
        return mapper.map(customers, List.class);
     }

     public Vendor toVendor(final VendorRequest vendorRequest) {
        return mapper.map(vendorRequest, Vendor.class);
     }
}
