package com.rental.PropertyRentalApi.Scripts;

import com.rental.PropertyRentalApi.Entity.Districts;
import com.rental.PropertyRentalApi.Repository.DistrictsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(6)
public class DistrictsSeeder implements CommandLineRunner {

    private final DistrictsRepository districtsRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding districts...");

        seedDistricts("districts1");
        seedDistricts("districts2");
        seedDistricts("districts3");

        log.info("Seed completed.");
    }

    private void seedDistricts(String districtName) {

        if (districtsRepository.existsByName(districtName)) {
            log.info("District '{}' already exist. Skipping.", districtName);
            return;
        }

        districtsRepository.save(
                Districts.builder()
                        .name(districtName)
                        .build()
        );
    }
}
