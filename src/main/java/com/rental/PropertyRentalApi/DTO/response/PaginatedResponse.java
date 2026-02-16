package com.rental.PropertyRentalApi.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {
    private List<T> items;
    private PaginationMeta pagination;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaginationMeta {
        private int currentPage;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean hasNext;
        private boolean hasPrevious;
    }
}