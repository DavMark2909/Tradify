package com.tradify.application.dto;

public record CompanyProfileDto(
        String name,
        String description,
        long sector,
        boolean isSupplier,
        boolean isConsumer,
        boolean isLogistics
) {
}
