package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.Entity.Properties;
import com.rental.PropertyRentalApi.Entity.UploadsImages;
import com.rental.PropertyRentalApi.Repository.PropertyRepository;
import com.rental.PropertyRentalApi.Repository.UploadsImagesRepository;
import com.rental.PropertyRentalApi.Service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.*;

@Service
@RequiredArgsConstructor
public class UploadsImagesServiceImpl implements UploadService {

    private final UploadsImagesRepository uploadsImagesRepository;
    private final PropertyRepository propertyRepository;

    private final String UPLOAD_DIR = "uploads/";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

    @Override
    public String uploadImage(Long propertyId, MultipartFile file) {

        try {

            if (file.getSize() > 5 * 1024 * 1024) {
                throw badRequest("File size exceeds 5MB");
            }

            Properties properties = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> notFound("Property not found."));

            File uploadUrl = new File(UPLOAD_DIR);
            if (!uploadUrl.exists()) {
                uploadUrl.mkdir();
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.write(filePath, file.getBytes());

            UploadsImages image = new UploadsImages();
            image.setUrls(fileName);
            image.setProperty(properties);

            uploadsImagesRepository.save(image);

//            return "Image uploaded successfully";
            return "/uploads/" + fileName;

        } catch (IOException e) {
            throw internal("Failed to upload image", e);
        }
    }
}
