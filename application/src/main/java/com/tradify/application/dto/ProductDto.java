package com.tradify.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDto (
        long id,
        String title,
        String description,
        BigDecimal price,
        String currency,
        String measure,
        BigDecimal quantity,
        String status,
        String company,
        LocalDateTime createdAt
) {}
