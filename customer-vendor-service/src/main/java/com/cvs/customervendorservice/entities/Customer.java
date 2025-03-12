package com.cvs.customervendorservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Customer extends AbstractEntity {

    @Column(name = "fisrname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
