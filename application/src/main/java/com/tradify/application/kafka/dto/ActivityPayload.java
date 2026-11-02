package com.tradify.application.kafka.dto;

public record ActivityPayload(
        Long productId,
        Long sectorId,
        Long supplierCompanyId
) {
}
