package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.UploadsImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadsImagesRepository extends JpaRepository<UploadsImages, Long> {

//    Optional<UploadsImages> findByUserAndOwnerType(
//            Users user
//    );
}
