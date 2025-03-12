package com.cvs.customervendorservice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDetails {

    private String path;
    private String filename;
    private String contentType;
    private byte[] data;
}
