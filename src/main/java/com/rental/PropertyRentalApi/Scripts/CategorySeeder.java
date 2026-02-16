package com.rental.PropertyRentalApi.Scripts;

import com.rental.PropertyRentalApi.Entity.Categories;
import com.rental.PropertyRentalApi.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(3)
public class CategorySeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding categories...");

        seedCategories("apartment");
        seedCategories("house");
        seedCategories("condo");

        log.info("Seeding completed.");
    }

    private void seedCategories(String categoryName) {

        if (categoryRepository.existsByName(categoryName)) {
            log.info("Category '{}' already exists. Skipping.", categoryName);
            return;
        }


        categoryRepository.save(
                Categories.builder()
                        .name(categoryName)
                        .build()
        );

        log.info("Categories seed successfully.");
    }
}
