package com.as.addressservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@SuperBuilder
@Table(name = "address")
@EntityListeners(AuditingEntityListener.class)
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
