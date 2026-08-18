package com.tradify.application.dto;

import java.util.List;

public record HomeViewDto(
        List<ProductDto> trendingProducts,
        List<SavedItemDto> savedItemDtos
) {
}
