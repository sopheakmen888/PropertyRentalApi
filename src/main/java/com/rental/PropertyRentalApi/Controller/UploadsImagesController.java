package com.rental.PropertyRentalApi.Controller;

import com.rental.PropertyRentalApi.Service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/users/profile/{userId}")
    public ResponseEntity<?> uploadProfile(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(
                uploadService.uploadUserProfile(userId, file)
        );
    }
}
