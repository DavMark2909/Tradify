package com.tradify.application.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record HomeViewDto(
        List<ProductDto> trendingProducts,
        Page<SavedItemDto> savedItemDtos
) {
}
