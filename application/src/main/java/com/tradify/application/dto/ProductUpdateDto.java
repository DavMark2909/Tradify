package com.tradify.application.dto;

import java.math.BigDecimal;

public record ProductUpdateDto(
        String title,
        String description,
        BigDecimal price,
        String currency,
        String unitOfMeasure,
//        quantity is set to 0 from FE if create
        BigDecimal availableQuantity,
//        status is set to available by default
        String status
) {}
