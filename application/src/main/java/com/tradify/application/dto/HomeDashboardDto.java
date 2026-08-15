package com.tradify.application.dto;

import java.util.List;

public record HomeDashboardDto(
        List<ProductDto> products,
        List<SavedItemDto> savedProducts,
        List<TradeAgreementDto> tradeAgreements,
        UserProfileDto userProfileDto
) {
}
