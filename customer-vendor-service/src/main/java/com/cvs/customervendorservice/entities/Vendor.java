package com.cvs.customervendorservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Vendor extends AbstractEntity {

    private String vendorName;
    private String homePage;
    private boolean isDisqualified;

}
