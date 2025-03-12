package com.cvs.customervendorservice.web.dto;

import com.cvs.customervendorservice.web.model.AddressRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerRequest extends EntitySharedDto implements Serializable {

    private String id;
    private String lastName;
    private String firstName;
    private String status;
    private AddressRequest addressRequest;
}
