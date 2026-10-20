package com.tradify.application.kafka.listeners;

import com.tradify.application.kafka.dto.ProductEvent;
import com.tradify.application.kafka.producers.ProductEventProducer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
public class ProductEventListener {

    private final ProductEventProducer productEventProducer;

//    fires only once the product is committed, so a rollback never reaches Kafka
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProductEvent(ProductEvent event) {
        productEventProducer.publish(event);
    }
}
