package com.tradify.application.dto;

public record CompanyProfileDto(
        String name,
        String description,
        long sector,
        byte isSupplier,
        byte isConsumer,
        byte isLogistics
) {
}
