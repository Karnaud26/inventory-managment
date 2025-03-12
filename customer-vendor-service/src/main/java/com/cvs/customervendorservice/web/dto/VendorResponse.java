package com.cvs.customervendorservice.web.dto;


import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VendorResponse extends EntitySharedDto implements Serializable {

    private String customerId;
    private String vendorName;
    private String homePage;
    private boolean isDisqualified;
    private String customerType;
}
