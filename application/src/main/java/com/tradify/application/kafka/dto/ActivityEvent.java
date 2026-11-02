package com.tradify.application.kafka.dto;

import java.time.Instant;
import java.util.UUID;

public record ActivityEvent(
        String eventId,
        String eventType,
        String timestamp,
        int version,
        Long userId,
        Long companyId,
        ActivityPayload payload
) {
    public static final String PRODUCT_VIEWED = "PRODUCT_VIEWED";
    public static final String PRODUCT_SAVED = "PRODUCT_SAVED";

    private static final int SCHEMA_VERSION = 1;

    public static ActivityEvent productViewed(Long userId, Long companyId, ActivityPayload payload) {
        return of(PRODUCT_VIEWED, userId, companyId, payload);
    }

    public static ActivityEvent productSaved(Long userId, Long companyId, ActivityPayload payload) {
        return of(PRODUCT_SAVED, userId, companyId, payload);
    }

    private static ActivityEvent of(String eventType, Long userId, Long companyId, ActivityPayload payload) {
        return new ActivityEvent(
                UUID.randomUUID().toString(),
                eventType,
                Instant.now().toString(),
                SCHEMA_VERSION,
                userId,
                companyId,
                payload
        );
    }
}
