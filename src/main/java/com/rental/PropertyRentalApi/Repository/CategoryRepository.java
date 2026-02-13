package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
import java.util.Optional;

=======
>>>>>>> e4594a1 (add image favorite)
@Repository
public interface CategoryRepository extends JpaRepository<Categories, Long> {
    // You can add custom query methods if needed
    // Example: find category by name
<<<<<<< HEAD
     Optional<Categories> findByName(String name);

     boolean existsByName(String name);
=======
    // Optional<Categories> findByName(String name);
>>>>>>> e4594a1 (add image favorite)
}
