package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Long> {
    // You can add custom query methods if needed
    // Example: find category by name
    // Optional<Categories> findByName(String name);
}
