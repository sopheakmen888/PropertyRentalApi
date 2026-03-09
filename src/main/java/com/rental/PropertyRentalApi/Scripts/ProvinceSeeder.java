package com.rental.PropertyRentalApi.Scripts;

import com.rental.PropertyRentalApi.Entity.Provinces;
import com.rental.PropertyRentalApi.Repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(5)
public class ProvinceSeeder implements CommandLineRunner {

    private final ProvinceRepository provinceRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding provinces...");

        seedProvince("something");
        seedProvince("something2");
        seedProvince("something3");

        log.info("Seed completed.");
    }

    private void seedProvince(String provinceName) {

        if (provinceRepository.existsByName(provinceName)) {
            log.info("Province '{}' already exists. Skipping.", provinceName);
            return;
        }


        provinceRepository.save(
                Provinces.builder()
                        .name(provinceName)
                        .build()
        );

        log.info("Provinces seed successfully.");
    }
}
