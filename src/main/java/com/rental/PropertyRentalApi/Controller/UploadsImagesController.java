package com.rental.PropertyRentalApi.Controller;

import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UploadsImagesController {

    private final UploadService uploadService;

    @PostMapping("/uploads/{propertyId}")
    public ResponseEntity<?> uploadImages(
            @PathVariable Long propertyId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        List<String> imageUrls = uploadService.uploadPropertyImages(propertyId, files);
        return ResponseEntity.ok(imageUrls);
    }

    @DeleteMapping("/uploads/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable Long imageId) {
        uploadService.deleteImage(imageId);
        return ResponseEntity.ok("Image deleted successfully.");
    }

    @PostMapping("/users/profile")
    public ResponseEntity<?> uploadProfile(
            Authentication authentication,
            @RequestParam("file") MultipartFile file
    ) {

        Users user = (Users) authentication.getPrincipal();
        Long userId = user.getId();

        String imageUrl = uploadService.uploadUserProfile(userId, file);

        return ResponseEntity.ok(imageUrl);
    }
}
