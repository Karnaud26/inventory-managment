package com.cvs.customervendorservice.web.dto;

import com.cvs.customervendorservice.web.model.AddressRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VendorRequest extends EntitySharedDto implements Serializable {

    private String id;
    private String vendorName;
    private String homePage;
    private boolean isDisqualified;
    private String customerType;
    private AddressRequest addressRequest;
}
