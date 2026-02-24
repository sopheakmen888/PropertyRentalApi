package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.Entity.Properties;
import com.rental.PropertyRentalApi.Entity.UploadsImages;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Repository.PropertyRepository;
import com.rental.PropertyRentalApi.Repository.UploadsImagesRepository;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import com.rental.PropertyRentalApi.Service.CloudinaryService;
import com.rental.PropertyRentalApi.Service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.*;

@Service
@RequiredArgsConstructor
public class UploadsImagesServiceImpl implements UploadService {

    private final UploadsImagesRepository uploadsImagesRepository;
    private final PropertyRepository propertyRepository;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

    @Override
    public List<String> uploadPropertyImages(Long propertyId, List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            throw badRequest("No files provided.");
        }

        Properties properties = propertyRepository.findById(propertyId)
                .orElseThrow(() -> notFound("Property not found."));

        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {

            if (file.getSize() > MAX_FILE_SIZE) {
                throw badRequest("One of the files exceeds 5MB limit.");
            }

            try {

                Map<?, ?> uploadResult = cloudinaryService.upload(file, "folder");

                String imageUrl = uploadResult.get("secure_url").toString();
                String publicId = uploadResult.get("public_id").toString();

                String contentType = file.getContentType();

                if (contentType == null ||
                        (!contentType.equals("image/jpeg") &&
                                !contentType.equals("image/png") &&
                                !contentType.equals("image/jpg"))) {

                    throw badRequest("Only jpg, jpeg, png images are allowed.");
                }

                UploadsImages image = new UploadsImages();
                image.setUrls(imageUrl);
                image.setPublicId(publicId);
                image.setProperty(properties);

                uploadsImagesRepository.save(image);

                imageUrls.add(imageUrl);

            } catch (IOException e) {
                throw internal("Failed to upload image", e);
            }
        }

        return imageUrls;
    }

    @Override
    public String uploadUserProfile(Long userId, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw badRequest("No files provided.");
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> notFound("User not found."));

        try {
            Map<?, ?> uploadResult = cloudinaryService.upload(file, "folder");

            String imageUrl = uploadResult.get("source_url").toString();
            String publicId = uploadResult.get("public_id").toString();

            if (user.getProfileImagePublicId() != null) {
                cloudinaryService.delete(user.getProfileImagePublicId());
            }

            user.setProfileImageUrl(imageUrl);
            user.setProfileImagePublicId(publicId);

            userRepository.save(user);

            return imageUrl;

        } catch (IOException e) {
            throw internal("Failed to upload profile image!", e);
        }
    }

    @Override
    public void deleteImage(Long imageId) {
        UploadsImages image = uploadsImagesRepository.findById(imageId)
                .orElseThrow(() -> notFound("Image not found."));

        try {
            // Delete from Cloudinary first
            cloudinaryService.delete(image.getPublicId());

            // Then delete from database
            uploadsImagesRepository.delete(image);

        } catch (IOException e) {
            throw internal("Failed to delete image", e);
        }
    }
}