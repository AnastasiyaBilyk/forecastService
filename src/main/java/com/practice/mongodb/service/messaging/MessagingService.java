package com.practice.mongodb.service.messaging;

public interface MessagingService<K,V> {

    void sendToQueue(K topic, V data);
}
