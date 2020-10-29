package com.practice.mongodb.service.messaging;

public interface MessagingService<V> {

    void sendToQueue(String topic, V data);
}
