package com.cvs.customervendorservice.web.dto;


import com.cvs.customervendorservice.web.model.AddressResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerResponse extends EntitySharedDto implements Serializable {

    private String customerId;
    private String lastName;
    private String firstName;
    private String status;
    private AddressResponse addressResponse;
}
