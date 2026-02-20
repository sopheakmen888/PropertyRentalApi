package com.rental.PropertyRentalApi.Controller;

import com.rental.PropertyRentalApi.Service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class UploadsImagesController {

    private final UploadService uploadService;

    @PostMapping("/{propertyId}")
    public ResponseEntity<?> uploadImages(
            @PathVariable Long propertyId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        List<String> imageUrls = uploadService.uploadImages(propertyId, files);
        return ResponseEntity.ok(imageUrls);
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable Long imageId) {
        uploadService.deleteImage(imageId);
        return ResponseEntity.ok("Image deleted successfully.");
    }
}
