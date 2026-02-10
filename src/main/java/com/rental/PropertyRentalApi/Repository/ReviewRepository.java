package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.Properties;
import com.rental.PropertyRentalApi.Entity.Reviews;
import com.rental.PropertyRentalApi.Entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Reviews, Long> {

    // Check if user already reviewed this property
    boolean existsByUserAndProperty(Users user, Properties property);

    // Find a specific user's review for a property
    Optional<Reviews> findByUserAndProperty(Users user, Properties property);

    // Get all reviews for a property (paginated)
    Page<Reviews> findAllByProperty(Properties property, Pageable pageable);

    // Get all reviews by a user (paginated)
    Page<Reviews> findAllByUser(Users user, Pageable pageable);

    // Count reviews for a property
    long countByProperty(Properties property);
}