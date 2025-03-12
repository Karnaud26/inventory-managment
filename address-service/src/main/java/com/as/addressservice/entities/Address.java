package com.as.addressservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "address")
public class Address extends AbstractEntity{

    @Column(name = "address1")
    @NotNull
    private String address1;
    @Column(name = "address2")
    private String address2;
    private String zipCode;
    private String city;
    private String country;
    private String state;
    private String street;
}
