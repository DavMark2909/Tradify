package com.tradify.application.dto;

import java.math.BigDecimal;

public record ProductCreateDto(
        String title,
        String description,
        BigDecimal price,
        String currency,
        String unitOfMeasure,
        BigDecimal availableQuantity,
        String status,
        Long sectorId,
        Long supplierCompanyId
) {}
