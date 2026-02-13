package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.Properties;
import com.rental.PropertyRentalApi.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Properties, Long> {

    boolean existsByTitle(String title);

    // Find properties created by a specific user
    List<Properties> findAllByCreatedBy(Users user);

//    // Find properties by category
//    List<Properties> findByCategoryId(Long categoryId);
//
//    // Find properties by price range
//    List<Properties> findByPriceBetween(Double minPrice, Double maxPrice);
//
//    // Find properties by category and price range
//    List<Properties> findByCategoryIdAndPriceBetween(Long categoryId, Double minPrice, Double maxPrice);
}
