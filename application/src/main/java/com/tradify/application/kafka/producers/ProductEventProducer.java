package com.tradify.application.kafka.producers;

import com.tradify.application.kafka.dto.ProductEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ProductEventProducer {

    private static final String TOPIC = "product-events";
    private final KafkaTemplate<String, ProductEvent> kafkaTemplate;

    public ProductEventProducer(@Qualifier("productKafkaTemplate") KafkaTemplate<String, ProductEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishProductCreated(ProductEvent event) {
        String partitionKey = String.valueOf(event.payload().supplierCompanyId());

        CompletableFuture<SendResult<String, ProductEvent>> future =
                kafkaTemplate.send(TOPIC, partitionKey, event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish ProductEvent [id: {}] to Kafka: {}", event.eventId(), ex.getMessage());
            } else {
                log.info("Published ProductEvent [id: {}] to partition [{}] at offset [{}]",
                        event.eventId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }
        });
    }
}