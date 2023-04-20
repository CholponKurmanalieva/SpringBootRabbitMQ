package com.example.main.producer;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface DispatcherProducer {
    void produce(String queue, Message message);
}