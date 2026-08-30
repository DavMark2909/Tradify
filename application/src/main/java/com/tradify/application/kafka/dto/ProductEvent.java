package com.tradify.application.kafka.dto;

import java.time.Instant;
import java.util.UUID;

public record ProductEvent(
        String eventId,
        String eventType,
        String timestamp,
        int version,
        ProductPayload payload
) {
    public static ProductEvent created(ProductPayload payload) {
        return new ProductEvent(
                UUID.randomUUID().toString(),
                "PRODUCT_CREATED",
                Instant.now().toString(),
                1,
                payload
        );
    }
}
