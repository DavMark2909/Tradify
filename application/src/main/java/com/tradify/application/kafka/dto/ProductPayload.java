package com.tradify.application.kafka.dto;

import java.math.BigDecimal;

public record ProductPayload(
        Long productId,
        Long supplierCompanyId,
        Long sectorId,
        String title,
        String description,
        BigDecimal price,
        String currency,
        String unitOfMeasure,
        BigDecimal availableQuantity,
        String status
) {}