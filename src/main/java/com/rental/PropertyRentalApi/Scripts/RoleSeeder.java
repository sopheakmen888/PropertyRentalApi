package com.rental.PropertyRentalApi.Scripts;

import com.rental.PropertyRentalApi.Entity.Roles;
import com.rental.PropertyRentalApi.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@RequiredArgsConstructor
@Order(1)
@SuppressWarnings("unused")
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        log.info("Seeding...");


        seed("admin");
        seed("user");
        seed("agent");

        log.info("Seeding completed.");
    }

    private void seed(String roleName) {
        if (roleRepository.existsByName(roleName)) {
            log.info("This role already exist.");
            return;
        }

        roleRepository.save(
                Roles.builder()
                        .name(roleName)
                        .build()
        );

        log.info("Role seed successfully.");
    }
}
