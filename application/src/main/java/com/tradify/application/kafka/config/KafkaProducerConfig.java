package com.tradify.application.kafka.config;

import com.tradify.application.kafka.dto.ActivityEvent;
import com.tradify.application.kafka.dto.ProductEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    private Map<String, Object> getBaseConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        props.put(JacksonJsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        return props;
    }

    @Bean(name = "productProducerFactory")
    public ProducerFactory<String, ProductEvent> productProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getBaseConfig());
    }

    @Bean(name = "productKafkaTemplate")
    public KafkaTemplate<String, ProductEvent> productKafkaTemplate(
            @Qualifier("productProducerFactory") ProducerFactory<String, ProductEvent> factory) {
        return new KafkaTemplate<>(factory);
    }

//    configuring producer for tracking event activity
    @Bean(name = "activityProducerFactory")
    public ProducerFactory<String, ActivityEvent> activityProducerFactory() {
        Map<String, Object> props = getBaseConfig();
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        props.put(ProducerConfig.LINGER_MS_CONFIG, 20);           // small batching window
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 1000);      // if Kafka is down, never block a user request for long
        return new DefaultKafkaProducerFactory<>(props);
    }

//    template for event activity tracking
    @Bean(name = "activityKafkaTemplate")
    public KafkaTemplate<String, ActivityEvent> activityKafkaTemplate(
            @Qualifier("activityProducerFactory") ProducerFactory<String, ActivityEvent> factory) {
        return new KafkaTemplate<>(factory);
    }
}
