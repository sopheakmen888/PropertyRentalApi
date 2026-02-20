package com.rental.PropertyRentalApi.Service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadService {

    List<String> uploadImages(Long propertyId, List<MultipartFile> files);

    void deleteImage(Long imageId);
}
