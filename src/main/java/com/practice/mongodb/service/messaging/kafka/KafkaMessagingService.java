package com.practice.mongodb.service.messaging.kafka;

import com.practice.mongodb.document.Forecast;
import com.practice.mongodb.service.messaging.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class KafkaMessagingService implements MessagingService<Forecast> {

    private final KafkaTemplate<String, Forecast> kafkaTemplate;

    @Autowired
    public KafkaMessagingService(KafkaTemplate<String, Forecast> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendToQueue(String topic, Forecast data) {
        ListenableFuture<SendResult<String, Forecast>> future = kafkaTemplate.send(topic, data);
        future.addCallback(System.out::println, System.err::println); //TODO: add logging of callbacks
        kafkaTemplate.flush();
    }
}
