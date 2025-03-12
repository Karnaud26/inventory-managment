package com.cvs.customervendorservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
abstract class AbstractEntity implements Serializable {

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

    @Column(name = "code", unique = true)
    private String code;

    // Image-related fields
    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "image_filename")
    private String imageFilename;

    @Column(name = "image_content_type")
    private String imageContentType;

    // Optional - store binary data directly for small images
    // Not recommended for large files - better to use a file system or specialized storage
    @Lob
    @Column(name = "image_data")
    private byte[] imageData;

    @NotNull
    @Column(unique = true)
    private String email;

    private String phone;

    private String address_id;

    @Column(name = "customerType")
    @Enumerated(EnumType.STRING)
    private CustomerType customerType;
}
