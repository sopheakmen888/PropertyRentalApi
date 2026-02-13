package com.rental.PropertyRentalApi.Repository;

import com.rental.PropertyRentalApi.Entity.Properties;
import com.rental.PropertyRentalApi.Entity.Reviews;
import com.rental.PropertyRentalApi.Entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Reviews, Long> {

    boolean existsByUserAndProperty(Users user, Properties property);

    Page<Reviews> findAllByProperty(Properties property, Pageable pageable);

    Page<Reviews> findAllByUser(Users user, Pageable pageable);
}
