package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.Users;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Users> findByUsername(String username);

    // Add this:
    Optional<Users> findByEmail(String email);

    @EntityGraph(attributePaths = {"favorites", "favorites.property"})
    Optional<Users> findById(@NonNull Long id);
}
