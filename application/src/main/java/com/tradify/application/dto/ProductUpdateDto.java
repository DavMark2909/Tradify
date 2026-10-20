package com.tradify.application.dto;

import java.math.BigDecimal;

public record ProductCreateDto(
        String title,
        String description,
        BigDecimal price,
        String currency,
        String unitOfMeasure,
        BigDecimal availableQuantity,
//        status is set to available by default
        String status
//        todo: change the endpoint to get the info about the company from the user profile
) {}
