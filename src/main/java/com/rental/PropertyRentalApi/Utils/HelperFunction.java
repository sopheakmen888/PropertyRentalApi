package com.rental.PropertyRentalApi.Utils;

import com.rental.PropertyRentalApi.Entity.Categories;
import com.rental.PropertyRentalApi.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.*;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class HelperFunction {

    private final CategoryRepository categoryRepository;



    // =================
    // GET CATEGORY
    // =================
    public Categories getCategoryOrThrow(String category) {
        return categoryRepository.findByName(category)
                .orElseThrow(() -> notFound("Category not found."));
    }


}
