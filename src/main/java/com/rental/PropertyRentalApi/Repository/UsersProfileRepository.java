package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.UsersProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersProfileRepository extends JpaRepository<UsersProfile, Long> {
}
