package com.rental.PropertyRentalApi.Scripts;

import com.rental.PropertyRentalApi.Entity.Commune;
import com.rental.PropertyRentalApi.Repository.CommuneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(4)
public class CommuneSeeder implements CommandLineRunner {

    private final CommuneRepository communeRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding Communes...");

        seedCommune("Toul Tompoung");
        seedCommune("Boeng Keng Kang I");
        seedCommune("Boeng Keng Kang II");

        log.info("Seeding completed.");
    }

    private void seedCommune(String communeName) {
        if (communeRepository.existsByName(communeName)) {
            log.info("Commune '{}' already exists. Skipping.", communeName);
            return;
        }

        communeRepository.save(
                Commune.builder()
                        .name(communeName)
                        .build()
        );
    }
}
