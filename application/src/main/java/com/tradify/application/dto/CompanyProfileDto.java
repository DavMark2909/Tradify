package com.tradify.application.dto;

public record CreateCompanyProfileDto(
        String name,
        String description,
        long sector,
        byte isSupplier,
        byte isConsumer,
        byte isLogistics
) {
}
