package com.rental.PropertyRentalApi.Scripts;

import com.rental.PropertyRentalApi.Entity.Roles;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Repository.RoleRepository;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.notFound;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(2)
@SuppressWarnings("unused")
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Seeding...");

        seedAdmin();

        log.info("Seeding completed");
    }

    private void seedAdmin() {
        String adminEmail = "admin.wmad@gmail.com";

        // Prevent duplicate admin
        if (userRepository.existsByEmail(adminEmail)) {
            log.info("Admin already exist.");
            return;
        }

        Roles adminRole = roleRepository.findByName("admin")
                .orElseThrow(() -> {
                    log.info("Failed to seed admin!");
                    return notFound("Role admin not found.");
                });

        Users admin = Users.builder()
                .fullname("Admin user")
                .username("admin")
                .email(adminEmail)
                .password(passwordEncoder.encode("Admin@123"))
                .roles(new HashSet<>(Set.of(adminRole)))
                .enabled(true)
                .build();

        userRepository.save(admin);
        log.info("Admin seeded successfully");
    }
}
