package com.as.addressservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractEntity implements Serializable {

    @Id
    @UuidGenerator
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    @CreatedBy
    @Column(updatable = false)
    private String createBy;

    @LastModifiedBy
    private String modifiedBy;
}
