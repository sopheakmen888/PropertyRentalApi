package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.RefreshToken;
import com.rental.PropertyRentalApi.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteAllByUsers(Users users);
}
