package com.rental.PropertyRentalApi.Controller;

import com.rental.PropertyRentalApi.Service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class UploadsImagesController {

    private final UploadService uploadService;

    @PostMapping("/{propertyId}")
    public ResponseEntity<?> uploadimage(
            @PathVariable Long propertyId,
            @RequestParam("file") MultipartFile file
    ) {
        String imageUrl = uploadService.uploadImage(propertyId, file);
        return ResponseEntity.ok(imageUrl);
    }
}
