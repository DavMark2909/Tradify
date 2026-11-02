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
//    listens to ApplicationEventPublisher. When it is fired, it checks the attached type and does routing based on it

    private final ProductEventProducer productEventProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProductEvent(ProductEvent event) {
        productEventProducer.publish(event);
    }
}
