package com.practice.mongodb.service.messaging.kafka;

import com.practice.mongodb.document.Forecast;
import com.practice.mongodb.service.messaging.MessagingService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class KafkaMessagingService implements MessagingService<String, Forecast> {

    private final KafkaTemplate<String, Forecast> kafkaTemplate;
    private final ConcurrentMap<String,Set<Forecast>> forecastsCache = new ConcurrentHashMap<>();

    @Autowired
    public KafkaMessagingService(KafkaTemplate<String, Forecast> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendToQueue(String key, Forecast data) {
        ListenableFuture<SendResult<String, Forecast>> future = kafkaTemplate.send("forecast", key, data);
        future.addCallback(System.out::println, System.err::println); //add logging of callbacks
        kafkaTemplate.flush();
    }

    @Override
    public Set<Forecast> getMessages(String key) {
        Set<Forecast> forecasts = new HashSet<>(forecastsCache.get(key));
        forecastsCache.get(key).removeAll(forecasts);
        return forecasts;
    }

    @KafkaListener(topics="forecast")
    public void listenForRecords(ConsumerRecord<String, Forecast> record){
        Set<Forecast> forecasts = forecastsCache.get(record.key());
        if ( forecasts == null) {
            forecasts = new HashSet<>();
            forecasts.add(record.value());
            forecastsCache.put(record.key(), forecasts);
        } else {
            forecasts.add(record.value());
        }
    }
}
