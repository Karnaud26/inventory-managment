package com.cvs.customervendorservice.service;

import com.cvs.customervendorservice.web.dto.ImageDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageStorageService {

    @Value("${app.customer.images.upload-dir:uploads/customer-images}")
    private String uploadDir;

    public ImageDetails storeImage(MultipartFile file) throws IOException {
        // Create directory if needed
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null ?
                originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String newFilename = UUID.randomUUID().toString() + fileExtension;

        // Save the file
        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return  ImageDetails.builder()
                .filename(newFilename)
                .contentType(file.getContentType())
                .path(filePath.toString())
                .data(file.getBytes())
                .build();
    }

    public void deleteImage(String imagePath) throws IOException {
        if (imagePath != null && !imagePath.isEmpty()) {
            Path path = Paths.get(imagePath);
            Files.deleteIfExists(path);
        }
    }
}
