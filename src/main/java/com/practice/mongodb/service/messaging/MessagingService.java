package com.practice.mongodb.service.messaging;

import java.util.Set;

public interface MessagingService<K,V> {

    void sendToQueue(K key, V data);

    Set<V> getMessages(K key);
}
