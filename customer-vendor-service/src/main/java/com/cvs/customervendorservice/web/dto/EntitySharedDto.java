package com.cvs.customervendorservice.web.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EntitySharedDto {

    private String email;
    private String customerType;
    private String phone;
    private String code;
    private byte[] image;
    private String address_id;
    private String imagePath;
    private String imageFilename;
    private String imageContentType;
    private byte[] imageData;
}
