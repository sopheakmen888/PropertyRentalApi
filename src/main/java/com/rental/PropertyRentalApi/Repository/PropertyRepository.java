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
}
