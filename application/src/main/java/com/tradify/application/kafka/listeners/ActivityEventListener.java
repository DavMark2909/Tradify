package com.tradify.application.kafka.listeners;

import com.tradify.application.kafka.dto.ActivityEvent;
import com.tradify.application.kafka.producers.ActivityEventProducer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
public class ActivityEventListener {

    private final ActivityEventProducer activityEventProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onActivityEvent(ActivityEvent event) {
        activityEventProducer.publish(event);
    }
}