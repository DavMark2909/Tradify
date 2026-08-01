package com.tradify.application.dto;

public record UpdateCompanyProfileDto(
        String name,
        String description,
        String sector,
        byte isSupplier,
        byte isConsumer,
        byte isLogistics
) {
}
