package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.Favorites;
import com.rental.PropertyRentalApi.Entity.Properties;
import com.rental.PropertyRentalApi.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, Long> {

    // Check if a user already favorited a property
    boolean existsByPropertyAndUser(Properties property, Users user);

    // Find the favorite entity by property and user
    Optional<Favorites> findByPropertyAndUser(Properties property, Users user);
}
