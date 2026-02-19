package com.rental.PropertyRentalApi.Service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    String uploadImage(Long propertyId, MultipartFile file);
}
