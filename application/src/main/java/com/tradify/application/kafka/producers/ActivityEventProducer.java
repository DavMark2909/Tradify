package com.tradify.application.kafka.producers;

import com.tradify.application.kafka.dto.ActivityEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ActivityEventProducer {

    private static final String TOPIC = "activity-events";
    private final KafkaTemplate<String, ActivityEvent> kafkaTemplate;

    public ActivityEventProducer(@Qualifier("activityKafkaTemplate") KafkaTemplate<String, ActivityEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(ActivityEvent event) {
        String partitionKey = event.userId() != null ? String.valueOf(event.userId()) : null;
        try {
            kafkaTemplate.send(TOPIC, partitionKey, event)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.warn("Failed to publish ActivityEvent [id: {}, type: {}]: {}",
                                    event.eventId(), event.eventType(), ex.getMessage());
                        } else {
                            log.debug("Published ActivityEvent [id: {}] to partition [{}] at offset [{}]",
                                    event.eventId(),
                                    result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        }
                    });
        } catch (Exception e) {
            log.warn("Could not send ActivityEvent [id: {}]: {}", event.eventId(), e.getMessage());
        }
    }
}